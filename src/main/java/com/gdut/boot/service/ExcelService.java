package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/11/181:06
 */
@Service
public interface ExcelService {

    Msg importOnInThree(InputStream inputStream, int type);

    String getDownLoadPath(int type);

    String getExcelOutportFileName(int type);

    Object getDate(int type);

    Msg dealBeansAfter(List<Object> beans, int type) throws Exception;

    Msg dealBeansBefore(List<Object> beans, int value) throws Exception;
}
