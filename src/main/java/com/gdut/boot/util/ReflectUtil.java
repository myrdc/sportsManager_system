package com.gdut.boot.util;

import java.lang.reflect.Method;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/271:09
 */

public class ReflectUtil {

    public static Method getMethod(Class aClass, String name, Class...args) throws NoSuchMethodException {
        return aClass.getMethod(name, args);
    }

    public static Class getClass(String name) throws Exception {
        return Class.forName(name);
    }
}
