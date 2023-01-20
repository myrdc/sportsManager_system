package com.gdut.boot.constance.permission;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/5/241:53
 */

public enum Resources {
    POST("post", 1),
    GET("get", 2),
    PUT("put", 4),
    DELETE("delete", 3),
    ALL("all", 5)
    ;
    //资源
    private String name;
    private Integer type;

    Resources(String name, Integer type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Integer getType() {
        return type;
    }

    public static Resources getResourcesById(int id){
        if(id == POST.getType()){
            return POST;
        }else if(id == GET.getType()){
            return GET;
        }else if(id == PUT.getType()){
            return PUT;
        }else if(id == DELETE.getType()){
            return DELETE;
        }else if(id == ALL.getType()){
            return ALL;
        }else{
            return null;
        }
    }


}
