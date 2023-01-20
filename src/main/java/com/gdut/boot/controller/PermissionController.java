package com.gdut.boot.controller;

import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.annotation.group.NotNeedAuth;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.ResultTwo;
import com.gdut.boot.service.PermissionService;
import com.gdut.boot.service.UserPermissionService;
import com.gdut.boot.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/4/2012:53
 */

@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 根据id查询
     * @param number
     * @return
     */
    @RequestMapping("getByNumber")
    @NotNeedAuth
    public Msg getByMsg(String number, HttpServletRequest request){
        //加密字符串
        if(CommonUtil.isEmpty(number)){
            return Msg.fail("不能传入一个空的字符串");
        }
        return permissionService.getPermission(number, request);
    }

    /**
     * 设置权限
     */
    @PutMapping("setAuth")
    @AuthName(name = "setAuth")
    public Msg setAuth(String number, String resName){
        return permissionService.setAuth(number, resName);
    }


    /**
     * 获取所有的
     */
    @GetMapping("getAllNumAuth")
    @AuthName(name = "getAllNumAuth")
    public Msg getAllNumAuth(@RequestParam(value = "pn", defaultValue = "1") Long pn,
                             @RequestParam(value = "size", defaultValue = "10") Long size){
        return permissionService.getAllNumAuth(pn, size);
    }

    /**
     * 根据账号获取单个
     */
    @GetMapping("getSingle")
    @AuthName(name = "getSingle")
    public Msg getByNumber(String number){
        return permissionService.getByNumber(number);
    }

    /*
        设置身份
     */
    @PostMapping("insertSingle")
    @AuthName(name = "insertSingle")
    public Msg postSingle(ResultTwo resultTwo){
        return permissionService.postSingle(resultTwo);
    }
}
