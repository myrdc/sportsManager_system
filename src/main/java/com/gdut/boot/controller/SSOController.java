package com.gdut.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdut.boot.annotation.group.NotNeedAuth;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.TicketDto;
import com.gdut.boot.service.PermissionService;
import com.gdut.boot.util.HttpUtil;
import com.gdut.boot.util.JwtUtil;
import com.gdut.boot.util.RedisUtils;
import com.gdut.boot.util.SSOClientUtil;
import com.mysql.cj.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gdut.boot.constance.common.RedisConstance.login;
import static com.gdut.boot.constance.common.RedisConstance.redis_login;

@RequestMapping("/api/sso")
@RestController
@Slf4j
public class SSOController {

    @Resource
    private PermissionService permissionService;

    @Resource
    RedisUtils redisUtils;

    final String TOKEN_PREFIX = "Token:";


    @NotNeedAuth
    @GetMapping(value = "subLogin")
    public Msg subLogin(String ticket, HttpServletRequest request
    ) throws Exception {
        //有ticket，则校验
        if(!StringUtils.isNullOrEmpty(ticket)){
            //1.解析出ticket中的学号
            String number = JwtUtil.getPayloadByKey(ticket, "number");
            //！！！！！此处改成查询自己数据库的用户，并返回用户的登录信息给前端
            Msg res = permissionService.getPermission(number, request);

            //0. 先验证是否是SSO页面颁发的，远程调用
            //发送请求，server端（统一认证中心）会返回一个字符串
            String httpUrl= SSOClientUtil.SERVER_URL_PREFIX+"/verify";
            //参数
            Map<String,String> params=new HashMap<String, String>();
            String token = (String) res.getOther();
            log.debug("token: {}", token);
            //这里放入clientLogoutUrl和token这两个参数是为了单点注销做准备
            params.put("ticket",ticket);
            params.put("clientUrl", SSOClientUtil.CLIENT_LOGOUT_URL);
            params.put("token",token);
            String isVerify = HttpUtil.sendHttpRequest(httpUrl, params);
            System.out.println("校验结果是："+ isVerify);
            //验证有效，再返回给前端
            if("true".equals(isVerify)){
                //白名单机制，登陆的时候存token到服务器，然后判断有没有token。有就是登陆成功
                redisUtils.set(TOKEN_PREFIX +token,1, 24 *  60 * 60);
                return res;
            }else {
                //删除
                redisUtils.del(redis_login + token);
                return Msg.fail("校验失败");
            }
        }
        //没有ticket，则告诉前端需要跳转
        //！！！注意状态码得是401，前端那边拦截了，401才跳转
        else{
            return Msg.UNKNOW();
        }
    }

    @NotNeedAuth
    @GetMapping(value = "getAuth")
    public Msg getAuth(String token, HttpServletRequest request
    ) {
        //有ticket，则校验
        if(!StringUtils.isNullOrEmpty(token)){
            return permissionService.getByToken(token, request);
        }
        //没有ticket，则告诉前端需要跳转
        //！！！注意状态码得是401，前端那边拦截了，401才跳转
        else{
            return Msg.UNKNOW();
        }
    }

    /**
     * 登出操作
     * 销毁掉token
     * @return
     */
    @RequestMapping("logout")
    @NotNeedAuth
    public Msg logout(String token){
        if(token!=null) {
            redisUtils.del(TOKEN_PREFIX +token);
            redisUtils.del(redis_login + token);
            return Msg.success();
        }
        return Msg.fail("token丢失");
    }

}
