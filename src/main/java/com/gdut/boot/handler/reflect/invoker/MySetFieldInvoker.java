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

public class MySetFieldInvoker implements Invoker {
    private final Field field;

    private final Method setMethod;

    public MySetFieldInvoker(Field field, Method method) {
        if(Reflector.canControlMemberAccessible()){
            field.setAccessible(true);
        }
        this.field = field;
        this.setMethod = method;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        try {
            setMethod.invoke(target, args[0]);
        } catch (IllegalAccessException | InvocationTargetException e) {
            if (Reflector.canControlMemberAccessible()) {
                setMethod.invoke(target, args[0]);
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
