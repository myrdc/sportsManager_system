package com.gdut.boot.handler.reflect.invoker;

import com.gdut.boot.util.BeanUtils;
import org.apache.ibatis.reflection.Reflector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/4/50:44
 */

public class ServiceInvoker implements MyInvoker {

    private Class<?> serviceClass;
    private Object serviceObject;

    public ServiceInvoker(Class<?> serviceClass) throws Exception {
        this.serviceClass = serviceClass;
        serviceObject = BeanUtils.getBean(serviceClass);
        if(serviceObject == null){
            throw new Exception("生成serviceObject异常");
        }
    }

    @Override
    public Object invoke(String methodName, Object[] args, Class... clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Method method = serviceClass.getMethod(methodName, clazz);
        try {
            return method.invoke(serviceObject, args);
        } catch (IllegalAccessException e) {
            if (Reflector.canControlMemberAccessible()) {
                method.setAccessible(true);
                return method.invoke(serviceObject, args);
            } else {
                throw e;
            }
        }
    }

    @Override
    public Class<?> getType() {
        return serviceClass;
    }

    public Object getServiceObject(){
        return serviceObject;
    }
}
