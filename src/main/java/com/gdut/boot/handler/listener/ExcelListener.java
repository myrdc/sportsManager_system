package com.gdut.boot.handler.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.handler.reflect.MyReflector;
import com.gdut.boot.handler.reflect.invoker.ServiceInvoker;
import com.gdut.boot.service.ExcelService;
import com.gdut.boot.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static com.gdut.boot.constance.cache.Cache.reflectors;
import static com.gdut.boot.constance.common.Common.INSERT;
import static com.gdut.boot.util.CommonUtil.bindData;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 周来料出货和出货的导入
 * @verdion
 * @date 2021/11/2519:23
 */
@Slf4j
@Component
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class ExcelListener extends AnalysisEventListener {


    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

    private static Map<String, List<Object>> map = new HashMap<>();

    @Override
    public void invoke(Object entity, AnalysisContext analysisContext) {
        REENTRANT_LOCK.lock();
        try {
            //获取类的名字
            String simpleName = entity.getClass().getSimpleName();
            ArrayList<Object> objects = null;
            if (map.get(simpleName) == null) {
                //创建一个
                objects = new ArrayList<>();
                map.put(simpleName, objects);
            } else {
                objects = (ArrayList<Object>) map.get(simpleName);
            }
            objects.add(entity);
        } catch (Exception e) {
            throw e;
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        REENTRANT_LOCK.lock();
        try {
            //插入
            String simpleName = analysisContext.readWorkbookHolder().getClazz().getSimpleName();
            List<Object> beans = map.get(analysisContext.readWorkbookHolder().getClazz().getSimpleName());
            if(beans.size() == 0){
                return;
            }
            ExcelService excelService = BeanUtils.getBean(ExcelService.class);
            //dealWithbeans
            //获取service执行器, 首先插入数据库
            Msg msg = excelService.dealBeansBefore(beans, AllClass.valueOf(simpleName).getValue());
            for (Object bean : beans) {
                if(bindData(bean) == true){
                    throw new BusinessException(Msg.fail("导入的excel中运动员数据缺少"));
                }
            }
            if(msg.getCode() == Msg.fail().getCode()){
                throw new BusinessException(msg, "doAfterAllAnalysed", null,
                        Arrays.asList(analysisContext),
                        reflectors.get(AllClass.valueOf(analysisContext.readWorkbookHolder().getClazz().getSimpleName()).getValue()).getType());
            }
            ServiceInvoker serviceInvoker = reflectors.get(AllClass.valueOf(simpleName).getValue()).getServiceInvoker();
            serviceInvoker.invoke(INSERT + simpleName, new Object[]{beans}, List.class);
            //如果到这里肯定可以执行成功了
            msg = excelService.dealBeansAfter(beans, AllClass.valueOf(simpleName).getValue());
            if(msg.getCode() == Msg.fail().getCode()){
                throw new BusinessException(msg, "doAfterAllAnalysed", null,
                        Arrays.asList(analysisContext),
                        reflectors.get(AllClass.valueOf(analysisContext.readWorkbookHolder().getClazz().getSimpleName())).getType());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }  finally {
            REENTRANT_LOCK.unlock();
            map.clear();
        }
    }
}
