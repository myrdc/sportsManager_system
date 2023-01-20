package com.gdut.boot.handler.reflect.wrapper;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JLHWASX
 * @Description 注解的封装
 * @verdion
 * @date 2022/3/2721:11
 */

@Getter
public class AnnotationWrapper {

    private final Map<String, List<Annotation>> annotations;

    public AnnotationWrapper(Class<?> clazz) {
        annotations = new HashMap<>();
        beAnnotationMap(clazz);
    }

    private void beAnnotationMap(Class<?> clazz) {
        while(clazz != null && clazz != Object.class){
            for (Field field : clazz.getDeclaredFields()) {
                //如果已经存在了就不再加入了, 父类的注解无法进入
                if(!annotations.containsKey(field.getName())){
                    //putIfAbsent表示如果父类也有那么就不替换
                    Annotation[] ans = field.getAnnotations();
                    //获取注解
                    List<Annotation> save = Arrays.asList(ans);
                    annotations.put(field.getName(), save);
                }
            }
            //往父类上遍历
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 这个参数有没有包含这个注解
     * @param fieldName
     * @param annotation
     * @return
     */
    public boolean hasAnnotation(String fieldName, Class annotation){
        List<Annotation> annotations = this.annotations.get(fieldName);
        if(annotations == null || annotations.isEmpty()){
            return false;
        }
        return annotations.contains(annotation);
    }
}
