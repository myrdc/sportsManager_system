package com.gdut.boot.handler.reflect.invoker;

import java.lang.reflect.InvocationTargetException;

public interface MyInvoker {
  Object invoke(String methodName, Object[] args, Class... clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

  Class<?> getType();
}