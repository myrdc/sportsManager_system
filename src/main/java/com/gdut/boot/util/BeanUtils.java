package com.gdut.boot.util;

/**
 * @description:
 * @author: HP
 * @date: 2020-07-17 18:28
 */

import com.gdut.boot.constance.common.AllClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import static com.gdut.boot.constance.common.Common.*;

/**

 * Spring ApplicationContext 工具类

 */

@SuppressWarnings("unchecked")

@Component
@Slf4j
public class BeanUtils implements ApplicationContextAware {



    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        synchronized (BeanUtils.class){
            BeanUtils.applicationContext = applicationContext;
        }
    }



    public static <T> T getBean(String beanName) {
        synchronized (BeanUtils.class){
            if(applicationContext.containsBean(beanName)){
                return (T) applicationContext.getBean(beanName);
            }else{

                return null;
            }
        }
    }



    public static <T> java.util.Map<String, T> getBeansOfType(Class<T> baseType){
        synchronized (BeanUtils.class) {
            return applicationContext.getBeansOfType(baseType);
        }
    }

    public static <T> T getBean(Class<T> baseType) {
        synchronized (BeanUtils.class) {
            return applicationContext.getBean(baseType);
        }
    }

    public static Object classForBean(String name) throws Exception {
        return Class.forName(CLASS_PREFFIX + name).newInstance();
    }

    public static Class classForVo(String name) throws Exception {
        return Class.forName(CLASS_VO_PREFFIX + name + VO);
    }

    public static String toImgSrc(String fileName){
        return fileName;
    }

    public static String toDocSrc(String fileName){
        return fileName;
    }

    //是不是文件bean
    public static boolean judgeIsFileBean(int type){
        if(type == AllClass.CoachManage.getValue() ||
                type ==AllClass.CompetitionManage.getValue() ||
                type == AllClass.SportsBaseMsg.getValue() ||
                type == AllClass.EntranceInfo.getValue() ||
                type == AllClass.EntranceExamMsg.getValue()){
            return true;
        }
        return false;
    }
}

