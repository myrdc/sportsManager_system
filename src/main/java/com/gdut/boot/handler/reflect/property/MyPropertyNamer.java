package com.gdut.boot.handler.reflect.property;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * @author JLHWASX
 * @Description 属性的一些工具类
 * @verdion
 * @date 2022/3/27 16:09
 */

public class MyPropertyNamer {

    private MyPropertyNamer() {

    }

    /**
     * get,set方法名字转换成属性名字
     * @param name 方法名字
     * @return
     */
    public static String methodToProperty(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        //如果是bool属性, get方法是is开头的
        if(name.startsWith("is")){
            name.substring(2);
        }else if(name.startsWith("get") || name.startsWith("set")){
            name.substring(3);
        }else{
            return null;
        }
        return name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
    }

    /**
     * 是不是get或者set方法
     * @param name 方法名字
     * @return
     */
    public static boolean isProperty(String name){
        return isGet(name) || isSet(name);
    }

    /**
     * 是否是get方法
     * @param name 名字
     * @return
     */
    public static boolean isGet(String name){
        return name.startsWith("get") && name.length() > 3 || name.startsWith("is") && name.length() > 2;
    }

    /**
     * 是否是set方法
     * @param name 名字
     * @return
     */
    public static boolean isSet(String name){
        return name.startsWith("set") && name.length() > 3;
    }

    /**
     * 属性名字变成数据库名字
     */
    public static String propertyReverseToDBName(String name){
        if (name == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int dbLength = name.length();
        for (int i = 0; i < dbLength; i++) {
            //获取字符
            char c = name.charAt(i);
            //判断
            if (c >= 'A' && c <= 'Z') {
                //遇到大写,就把全部的变小写
                sb.append("_").append((char) (c + 32));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 数据库表名转化成类名
     * @param db 数据库表名
     * @return 类名
     */
    public static String dbNameReverseToClassName(String db) {
        if (db == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        //分割
        String[] s = db.split("_");
        for (int i = 0; i < s.length; i++) {
            sb.append(s[i].substring(0, 1).toUpperCase()).append(s[i].substring(1));
        }
        return sb.toString();
    }

    /**
     * @param db 类名
     * @return
     * @Description 将实体类类名转化为数据库的表名
     */
    public static String classNameReverseToDBName(String db) {
        if (db == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < db.length(); i++) {
            char c = db.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                //转化为小写
                c = (char) (c + 32);
                //如果大写字母不是在首个，加_
                if (i != 0) {
                    sb.append('_');
                }
                sb.append(c);
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * @param db 传入的数据
     * @return 返回转化后的数据
     */
    public static String dbNameReverseToProperty(String db) {
        if (db == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        //分割
        String[] s = db.split("_");
        for (int i = 0; i < s.length; i++) {
            if (i != 0) {
                s[i] = s[i].toLowerCase();
            }
            if (i == 0) {
                //拼接
                sb.append(s[0]);
            } else {
                //拼接
                sb.append(s[i].substring(0, 1).toUpperCase()).append(s[i].substring(1));
            }
        }
        return sb.toString();
    }
}
