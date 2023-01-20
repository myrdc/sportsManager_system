package com.gdut.boot.annotation.xss;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author myrdc
 * @Description 注解参数，对表上注解的字段进行校验
 * @verdion
 * @date 2021/7/319:27
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventXSSField {
}
