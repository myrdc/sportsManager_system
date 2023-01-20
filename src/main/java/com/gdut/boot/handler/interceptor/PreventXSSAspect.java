package com.gdut.boot.handler.interceptor;
import com.gdut.boot.annotation.xss.PreventXSSField;
import com.gdut.boot.annotation.xss.PreventXSSMethod;
import com.gdut.boot.annotation.xss.PreventXSSParameter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author JLHWASX
 * @Description
 * @verdion
 * @date 2021/7/319:32
 */

@Aspect
@Component
public class PreventXSSAspect {

    //对所有的controller切入
    @Pointcut("execution(* com.gdut.boot.controller.*.*(..))")
    public void pointCut() {}

    //对于切入了controller方法的并且标有preventXSSMethod注解的方法进行检测
    @Around("pointCut() && @annotation(preventXSSMethod)")
    public Object around(ProceedingJoinPoint joinPoint, PreventXSSMethod preventXSSMethod) throws Throwable {
        //获取方法所有参数、注解等
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取所有参数上的注解（一个参数可能对应多个注解，因此获取到的是一个二维数组。）
        Annotation[][] paramsAnnotations = signature.getMethod().getParameterAnnotations();
        // 遍历一维数组，获取每个参数对应的注解数组
        for (int i = 0; i < paramsAnnotations.length; i++) {
            Annotation[] paramAnnotations = paramsAnnotations[i];
            // 遍历每个参数的注解数组
            for (Annotation annotation: paramAnnotations) {
                // 如果参数需要预防XSS攻击
                if (annotation instanceof PreventXSSParameter){
                    // 如果是String类型并且不是空，将其进行格式化
                    if (args[i] instanceof String && StringUtils.isNotEmpty((String) args[i])) {
                        args[i] = format((String) args[i]);
                    } else {
                        // 否则获取该类型的所有字段，对String类型的字段进行格式化
                        Class clazz = args[i].getClass();
                        // 获取类的所有字段
                        Field[] fields = clazz.getDeclaredFields();
                        for (Field field: fields) {
                            // 如果字段上有@PreventXSSField注解
                            if (field.getDeclaredAnnotation(PreventXSSField.class) != null) {
                                // 如果是字段不可访问，设置临时可访问
                                if (!field.isAccessible()) {
                                    field.setAccessible(true);
                                }
                                // 如果字段是字符串类型则进行格式化
                                Object fieldValue = field.get(args[i]);
                                if (fieldValue instanceof String && StringUtils.isNotEmpty((String) fieldValue)) {
                                    field.set(args[i], format((String) fieldValue));
                                }
                            }
                        }
                    }
                }
            }
        }
        //执行目标方法，将参数覆盖到到原方法
        Object proceed = joinPoint.proceed(args);
        return proceed;
    }

    /**
     * 对需要防范的字符串进行格式化
     */
    public String format(String xssStr) {
        return StringEscapeUtils.escapeHtml4(xssStr);
    }
}
