package com.gdut.boot.annotation.xss;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author myrdc
 * @Description 注解方法
 * @verdion
 * @date 2021/7/319:26
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventXSSMethod {

}
