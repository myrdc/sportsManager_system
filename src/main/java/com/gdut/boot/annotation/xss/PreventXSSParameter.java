package com.gdut.boot.annotation.xss;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author myrdc
 * @Description 注解参数，对标上注解的参数校验
 * @verdion
 * @date 2021/7/319:26
 */


@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventXSSParameter {
}
