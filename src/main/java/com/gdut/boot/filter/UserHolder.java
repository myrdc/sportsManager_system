package com.gdut.boot.filter;

import com.gdut.boot.entity.SportsBaseMsg;
import com.gdut.boot.vo.res.UserPermissionVo;

public class UserHolder {
    private static final ThreadLocal<UserPermissionVo> tl = new ThreadLocal<>();

    public static void saveUser(UserPermissionVo user){
        tl.set(user);
    }

    public static UserPermissionVo getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
