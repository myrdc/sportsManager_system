package com.gdut.boot.constance.common;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/270:28
 */

public class Common {

    //名字
    public static final String INVOKE_LINK = "请求执行处理器链";
    public static final String AUDIT_LINK = "审核处理器链";

    //类名
    public static final String REQUEST_MESSAGE = "RequestMessage";

    //普通参数
    public static final String SET = "set";
    public static final String Get = "get";
    public static final String VO = "Vo";
    public static final String ID = "id";
    public static final String INSERT = "insert";


    //前缀
    public static final String REQ_PREFFIX = "http://47.98.190.152:8090/sports";
    public static final String CLASS_PREFFIX = "com.gdut.boot.entity.";
    public static final String CLASS_VO_PREFFIX = "com.gdut.boot.vo.";
    public static final String PAGE_PREFFIX = "com.baomidou.mybatisplus.extension.service.";

    //mybatis-plus参数
    public static Class<?> QUERY_WRAPPER = null;
    public static Class<?> UPDATE_WRAPPER = null;
    public static Class<?> WRAPPER = null;
    public static final String LIST = "list";
    public static final String SAVE = "save";
    public static final String REMOVE = "remove";
    public static final String UPDATE = "update";
    public static final String PAGE = "page";
    public static final String ISERVICE = "IService";


    static{
        try {
            QUERY_WRAPPER = Class.forName("com.baomidou.mybatisplus.core.conditions.query.QueryWrapper");
            UPDATE_WRAPPER = Class.forName("com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper");
            WRAPPER = Class.forName("com.baomidou.mybatisplus.core.conditions.Wrapper");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
