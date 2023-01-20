package com.gdut.boot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.Account;
import com.gdut.boot.filter.UserHolder;
import com.gdut.boot.service.AccountService;
import com.gdut.boot.mapper.AccountMapper;
import com.gdut.boot.util.RedisUtils;
import com.gdut.boot.vo.res.UserPermissionVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

import static com.gdut.boot.constance.common.RedisConstance.redis_black;
import static com.gdut.boot.constance.common.RedisConstance.redis_login;

/**
 *
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private HttpServletRequest request;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public Msg loginOut(String number) {
        String token = request.getHeader("token");
        UserPermissionVo user = JSONObject.parseObject((String)redisUtils.get(redis_login + token), UserPermissionVo.class);
        if(!user.getUserNumber().equals(number)){
            //不相同，那么证明学号错误
            return Msg.fail("传入的学号有误");
        }
        //开始加入黑名单
        stringRedisTemplate.opsForValue().set(redis_black + token, user.getUserNumber(), Duration.ofDays(7));
        return Msg.success("成功");
    }
}




