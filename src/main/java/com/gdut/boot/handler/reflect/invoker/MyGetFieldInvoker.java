package com.gdut.boot.handler.reflect.invoker;

import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.invoker.Invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/4/92:05
 */

public class MyGetFieldInvoker implements Invoker {
    private final Field field;
    private final Method getMethod;

    public MyGetFieldInvoker(Field field, Method method) {
        if(Reflector.canControlMemberAccessible()){
            field.setAccessible(true);
        }
        this.field = field;
        this.getMethod = method;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException,InvocationTargetException {
        try {
            this.getMethod.invoke(target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            if (Reflector.canControlMemberAccessible()) {
                this.getMethod.invoke(target);
            } else {
                throw e;
            }
        }
        return null;
    }

    public Field getField() {
        return field;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
