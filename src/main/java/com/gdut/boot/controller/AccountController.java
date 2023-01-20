package com.gdut.boot.controller;

import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.service.AccountService;
import com.gdut.boot.vo.AccountVo;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static com.gdut.boot.constance.cache.Cache.reflectors;
import static com.gdut.boot.constance.common.Common.INVOKE_LINK;
import static com.gdut.boot.constance.cache.Cache.HANDLERS;
import static com.gdut.boot.util.MessageUtil.toPageContent;
import static com.gdut.boot.util.MessageUtil.toReqestMessage;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/270:30
 */

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @PutMapping("loginOut")
    @AuthName(name = "loginOut")
    //传入学号
    public Msg loginOut(String number){
        return accountService.loginOut(number);
    }
}
