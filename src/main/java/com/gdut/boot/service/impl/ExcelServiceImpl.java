package com.gdut.boot.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.handler.listener.ExcelListener;
import com.gdut.boot.handler.reflect.MyReflector;
import com.gdut.boot.handler.reflect.invoker.ServiceInvoker;
import com.gdut.boot.service.CoachManageService;
import com.gdut.boot.service.CompetitionManageService;
import com.gdut.boot.service.ExcelService;
import com.gdut.boot.service.SportsBaseMsgService;
import com.gdut.boot.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static com.gdut.boot.constance.cache.Cache.reflectors;
import static com.gdut.boot.constance.common.Common.LIST;
import static com.gdut.boot.constance.common.Common.WRAPPER;
import static com.gdut.boot.constance.common.FileConstance.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/11/181:06
 */
@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private SportsBaseMsgService sportsBaseMsgService;

    @Autowired
    private CoachManageService coachManageService;

    @Autowired
    private CompetitionManageService competitionManageService;


    @Override
    public Msg importOnInThree(InputStream inputStream, int type) {
        MyReflector myReflector = reflectors.get(type);
        //获取class类型
        Class clazz = myReflector.getType();
        //获取工作簿对象
        ExcelReaderBuilder read = EasyExcel.read(inputStream, clazz, new ExcelListener());
        //获取所有sheet
        ExcelReaderSheetBuilder sheet = read.sheet();
        //从第二行开始读取(下标0)
        sheet.headRowNumber(1).doRead();
        return Msg.success("读取excel成功");
    }

    @Override
    public String getDownLoadPath(int type) {
        if(type == AllClass.CoachManage.getValue()){
            return EXCEL_OUTPUT_COACHS;
        }
        if(type == AllClass.SportsBaseMsg.getValue()){
            return EXCEL_OUTPUT_SPORTS;
        }
        if(type == AllClass.CompetitionManage.getValue()){
            return EXCEL_OUTPUT_COMPETITIONS;
        }
        return null;
    }

    @Override
    public String getExcelOutportFileName(int type) {
        if(type == AllClass.CoachManage.getValue()){
            return "教练信息" + System.currentTimeMillis() + ".xlsx";
        }
        if(type == AllClass.SportsBaseMsg.getValue()){
            return "运动员信息" + System.currentTimeMillis() + ".xlsx";
        }
        if(type == AllClass.CompetitionManage.getValue()){
            return "比赛信息" + System.currentTimeMillis() + ".xlsx";
        }
        return null;
    }

    @Override
    public Object getDate(int type) {
        try {
            MyReflector myReflector = reflectors.get(type);
            //servivce类
            ServiceInvoker serviceInvoker = myReflector.getServiceInvoker();
            //service
            Object serviceObject = serviceInvoker.getServiceObject();
            Object invoke = serviceInvoker.invoke(LIST, null, null);
            return invoke;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Msg dealBeansAfter(List<Object> beans, int type) throws Exception {
        Object res = reflectors.get(type).getServiceInvoker().invoke("dealWithExcelBean", new Object[]{beans}, List.class);
        return (Msg)res;
    }

    @Override
    public Msg dealBeansBefore(List<Object> beans, int type) throws Exception {
        Object res = reflectors.get(type).getServiceInvoker().invoke("dealBeanBefore", new Object[]{beans}, List.class);
        return (Msg)res;
    }
}
