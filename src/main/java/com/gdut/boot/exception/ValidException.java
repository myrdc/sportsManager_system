package com.gdut.boot.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/5/2416:59
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidException extends RuntimeException{

    private String message;
    private Object vo;
    private Set<ConstraintViolation<Object>> set;

}
