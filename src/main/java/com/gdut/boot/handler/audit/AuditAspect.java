package com.gdut.boot.handler.audit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gdut.boot.annotation.group.*;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.ResourceName;
import com.gdut.boot.constance.cache.Cache;
import com.gdut.boot.constance.permission.Permission;
import com.gdut.boot.constance.permission.Resources;
import com.gdut.boot.exception.AuthException;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.filter.UserHolder;
import com.gdut.boot.util.JwtUtil;
import com.gdut.boot.util.RedisUtils;
import com.gdut.boot.vo.res.UserPermissionVo;
import org.apache.catalina.User;
import org.apache.commons.collections4.Get;
import org.apache.commons.collections4.Put;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

import static com.gdut.boot.constance.common.RedisConstance.redis_login;

/**
 * @author JLHWASX
 * @Description
 * @verdion
 * @date 2021/7/319:32
 */

@Aspect
@Component
@Order(value = 1)
public class AuditAspect {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    HttpServletRequest request;

    //对所有的controller切入
    @Pointcut("execution(* com.gdut.boot.controller.*.*(..))")
    public void pointCut() {
    }

    //对于切入了controller方法的并且标有注解的方法进行检测
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = null;
        List<Annotation> ans = null;
        //到这里就是需要权限了并且需要登陆了
        try {
            //获取方法所有参数、注解等
            Object[] args = joinPoint.getArgs();
            String name = joinPoint.getSignature().getDeclaringType().getSimpleName();
            signature = (MethodSignature) joinPoint.getSignature();
            // 获取所有参数上的注解（一个参数可能对应多个注解，因此获取到的是一个二维数组。）
            Annotation[] annotations = signature.getMethod().getAnnotations();
            //转化为list
            ans = Arrays.asList(annotations);
            //判断如果不需要权限校验，就直接跳过就行
            for (Annotation an : ans) {
                if(an instanceof NotNeedAuth){
                    //如果有这个标志，表示无序权限就可以访问
                    Object proceed = joinPoint.proceed(args);
                    return proceed;
                }
            }
            String token = request.getHeader("token");
            if(token == null){
                throw new AuthException("token丢失", signature.getMethod().getName(), ans, null);
            }
            //验证token
            //JwtUtil.verifyToken(token);

            //到这里token没问题了
            Object user = redisUtils.get(redis_login + token);
            if(user == null){
                throw new AuthException("没有获取token, 请先进入网站操作", signature.getMethod().getName(), ans, null);
            }
            UserPermissionVo loginUser = JSONObject.parseObject((String)user, UserPermissionVo.class);
            //下面登陆了，但是需要判断权限
            for (Annotation an : ans) {
                if(an instanceof AuthName){
                    //如果有这个标志，需要判断权限
                    if(checkPermission((AuthName)an, loginUser, name) == false){
                        throw new AuthException("你没有操作的权限", signature.getMethod().getName(), ans, loginUser);
                    }
                    Object proceed = joinPoint.proceed(args);
                    return proceed;
                }
            }
            Object proceed = joinPoint.proceed(args);
            return proceed;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            throw new AuthException("无效签名", signature.getMethod().getName(), ans, null);
        }catch (TokenExpiredException e){
            e.printStackTrace();
            throw new AuthException("token过期", signature.getMethod().getName(), ans, null);
        }catch (AlgorithmMismatchException e){
            e.printStackTrace();
            throw new AuthException("token算法不一致", signature.getMethod().getName(), ans, null);
        }catch (JWTDecodeException e){
            e.printStackTrace();
            throw new AuthException("token错误", signature.getMethod().getName(), ans, null);
        }
    }

    private boolean checkPermission(AuthName annotation, UserPermissionVo loginUser, String classNames) {
        String name = annotation.name();
        Map<String, List<ResourceName>> resourceMiddles = loginUser.getResourceMiddles();
        List<ResourceName> resourceNames = resourceMiddles.get(Cache.className.get(classNames));
        if(resourceNames == null){
            //没有权限
            return false;
        }
        //判断
        Set<String> collect = resourceNames.stream().map(ResourceName::getIntName).collect(Collectors.toSet());
        if(!collect.contains(name)){
            return false;
        }
        return true;
    }


    private HttpServletRequest getRequest(Object[] args) {
        for (Object arg : args) {
            if(arg instanceof HttpServletRequest){
                return (HttpServletRequest)arg;
            }
        }
        return null;
    }
}
