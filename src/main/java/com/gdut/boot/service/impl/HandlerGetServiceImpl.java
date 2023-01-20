package com.gdut.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdut.boot.bean.GetCondition;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.handler.reflect.MyReflector;
import com.gdut.boot.service.CompetitionManageService;
import com.gdut.boot.service.HandlerGetService;
import com.gdut.boot.service.VoService;
import com.gdut.boot.util.CommonUtil;
import com.gdut.boot.util.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;

import static com.gdut.boot.constance.cache.Cache.*;
import static com.gdut.boot.constance.common.Common.*;
import static com.gdut.boot.util.BeanUtils.judgeIsFileBean;
import static com.gdut.boot.util.CommonUtil.*;
import static com.gdut.boot.util.ReflectUtil.getMethod;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/301:59
 */
@Service
public class HandlerGetServiceImpl implements HandlerGetService {

    @Autowired
    private VoService voService;

    @Resource
    private CompetitionManageService competitionManageService;

    /**
     * @param requestMessage
     * @return
     */
    @Override
    public Msg get(RequestMessage requestMessage) {
        //反射器
        final MyReflector reflector = reflectors.get(requestMessage.getType());
        //获取了get的请求
        GetCondition condition = requestMessage.getGetCondition();
        //获取分页插件
        Page page = condition.getPage();
        //获取所有的条件
        Map<String, String> conditions = condition.getCondition();
        //获取初始时间和结束时间
        String startTime = condition.getStartTime();
        String endTime = condition.getEndTime();
        //list类型数组
        String[] listType = condition.getListType();
        //获取对象类型
        int type = requestMessage.getType();
        return this.nextGet(reflector, page, conditions, startTime, endTime, type, listType);
    }

    private Msg nextGet(MyReflector reflector, Page page, Map<String, String> conditions, String startTime, String endTime, int type, String[] listType) {
        Object service = reflector.getServiceInvoker().getServiceObject();
        try {
            //获取顶层iServive
            Class iServive = ReflectUtil.getClass(PAGE_PREFFIX + ISERVICE);
            //获取方法
            Method pageMethod = getMethod(iServive, PAGE, IPage.class, WRAPPER);
            //正常情况下获取queryWrapper
            QueryWrapper queryWrapper = dealWithWrapper(conditions, startTime, endTime, listType);
            //最终实现
            return realInvoke(page, pageMethod, queryWrapper, service, type, listType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isMyLeftType(int type, Map<String, String> conditions) {
        if(type == AllClass.CompetitionManage.getValue()){
            if(conditions.containsKey("coach")){
                return true;
            }
        }
        return false;
    }

    private Msg realInvoke(Page page, Method pageMethod, QueryWrapper queryWrapper, Object service, int type, String[] listType) throws Exception{
        //执行wapper
        Page result = (Page)pageMethod.invoke(service, page, queryWrapper);
        if(result!= null && result.getRecords().size() == 0){
            return Msg.fail("没找到对应的数据");
        }
        //处理找到的result
        if(isVoResType(type)){
            List<Object> voRes = toVo(result.getRecords(), type);
            result.setRecords(voRes);
        }
        return Msg.success().setData(result);
    }

    //普通result结果封装成Vo
    private List<Object> toVo(List records, int type) {
        List<Object> res = new LinkedList<>();
        for (Object record : records) {
            res.add(voService.toVo(record, type));
        }
        return res;
    }

    /**
     * 处理wrapper
     * @param conditions map
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param listType
     * @return
     */
    private QueryWrapper dealWithWrapper(Map<String, String> conditions, String startTime, String endTime, String[] listType) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Set<String> strings = conditions.keySet();
        for (String string : strings) {
            if("pn".equals(string) || "size".equals(string)){
                continue;
            }
            //TODO:可能要还要对时间进行处理
            String name = fieldNameReverseToDBName(string);
            //如果是list类型的，数据库存的是json，那么就使用like匹配
            if(listType != null && isContains(listType, string)){
                queryWrapper.like(name, conditions.get(string));
                continue;
            }
            queryWrapper.eq(name, conditions.get(string));
        }
        return queryWrapper;
    }
}
