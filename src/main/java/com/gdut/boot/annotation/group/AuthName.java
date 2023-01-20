package com.gdut.boot.annotation.group;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 接口的名字
 * @verdion
 * @date 2022/7/170:34
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthName {
    String name() default "";
}
