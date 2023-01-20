package com.gdut.boot.annotation.file;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 多个文件
 * @verdion
 * @date 2022/1/2717:21
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FileList {
}
