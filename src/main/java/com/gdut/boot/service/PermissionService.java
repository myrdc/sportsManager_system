package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.ResultTwo;
import com.gdut.boot.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Service
public interface PermissionService extends IService<Permission> {


    Msg getPermission(String number, HttpServletRequest request);

    Msg setAuth(String number, String auName);

    Msg getAllNumAuth(Long pn, Long size);

    Msg getByNumber(String number);

    Msg postSingle(ResultTwo resultTwo);

    Msg getByToken(String token, HttpServletRequest request);
}
