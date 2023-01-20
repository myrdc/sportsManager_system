package com.gdut.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdut.boot.annotation.file.File;
import com.gdut.boot.annotation.file.FileList;
import com.gdut.boot.annotation.file.Img;
import com.gdut.boot.annotation.file.ImgList;
import com.gdut.boot.bean.FileVo;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.entity.EntranceExamMsg;
import com.gdut.boot.entity.EntranceInfo;
import com.gdut.boot.entity.SportCompetition;
import com.gdut.boot.entity.SportsBaseMsg;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.handler.generalHandler.DeleteHandler;
import com.gdut.boot.handler.reflect.MyReflector;
import com.gdut.boot.handler.reflect.invoker.ServiceInvoker;
import com.gdut.boot.service.*;
import com.gdut.boot.util.FileUtils;
import com.gdut.boot.util.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static com.gdut.boot.constance.common.Common.*;
import static com.gdut.boot.constance.cache.Cache.*;
import static com.gdut.boot.util.BeanUtils.judgeIsFileBean;
import static com.gdut.boot.util.CommonUtil.getGet;
import static com.gdut.boot.util.CommonUtil.isSerialVersionUID;
import static com.gdut.boot.util.FileUtils.*;

/**
 * @author JLHWASX
 * @Description
 * @verdion
 * @date 2022/1/27 22:10
 */

@Service
public class HandlerDeleteServiceImpl implements HandlerDeleteService {

    @Autowired
    private SportsBaseMsgService sportsBaseMsgService;

    @Autowired
    private OneOrganizationService oneOrganizationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg delete(RequestMessage requestMessage) {
        Object vo = requestMessage.getVo();
        try {
            //通过 id 删除
            int type = requestMessage.getType();
            Object result = null;
            //删除数据库
            MyReflector reflector = reflectors.get(type);
            result = deleteInDB(reflector, type, vo, 0);
            //判断是不是需要删除文件的类型
            if (result != null && judgeIsFileBean(type)) {
                //删除文件记录，节约空间。。。。
                deleteFile(result);
            }
            return Msg.success("删除成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public Msg deleteBatch(List<Integer> ids, int type) {
        try {
            //删除数据库
            List<Object> results = new ArrayList<>();
            final MyReflector reflector = reflectors.get(type);
            for (Integer id : ids) {
                Object o = deleteInDB(reflector, type, null, id);
                if(o!= null){
                    results.add(o);
                }
            }
            if(results.size() == 0){
                return Msg.fail("没有找到对应的数据");
            }

            //判断是不是一级组织
            if(type == AllClass.OneOrganization.getValue()){
                for (Object o : results) {
                    oneOrganizationService.deleteTwoTable(o);
                }
            }

            //判断是不是需要删除文件的类型
            if (judgeIsFileBean(type)) {
                //删除文件记录，节约空间。。。。
                for (Object var : results) {
                    deleteFile(var);
                }
            }
            return Msg.success("删除成功");
        } catch (Exception e) {
            //回滚到之前的位置
            throw new RuntimeException(e);
        }
    }


    /**
     * 返回的是bean
     *
     * @param type
     * @param vo
     * @return
     */
    private Object deleteInDB(MyReflector reflector, int type, Object vo, int ids) throws Exception {
        //删除数据库的内容，通过 id 删除
        int id = 0;
        if (ids != 0) {
            id = ids;
        }else{
            id = (int) vo.getClass().getMethod(getGet(ID))
                    .invoke(vo);
        }
        //获取service类
        Class service = SERVICE_CLASS.get(type);
        //获取执行器
        ServiceInvoker sI = reflector.getServiceInvoker();
        //创建wrapper
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        //首先查出数据库中的实体类要用来删除文件
        List result = (List) service.getMethod(LIST, WRAPPER).invoke(sI.getServiceObject(), queryWrapper);

        //调用方法删除数据库
        service.getMethod(REMOVE, WRAPPER)
                .invoke(sI.getServiceObject(), queryWrapper);

        return result.isEmpty() ? null : result.get(0);
    }

}
