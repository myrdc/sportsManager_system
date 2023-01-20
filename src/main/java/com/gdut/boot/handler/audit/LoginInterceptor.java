package com.gdut.boot.handler.audit;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdut.boot.util.BeanUtils;
import com.gdut.boot.util.JwtUtil;
import com.gdut.boot.util.RedisUtils;
import com.mysql.cj.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录检查
 * 1、配置好拦截器要拦截哪些请求
 * 2、把这些配置放在容器中
 */
public class LoginInterceptor implements HandlerInterceptor {

    RedisUtils redisUtil = BeanUtils.getBean("redisUtils");
    final String TOKEN_PREFIX = "Token:";

    /**
     * 目标方法执行之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        Map<String,Object> map = new HashMap<>();
        request.getPathInfo();

        //token存储在请求头里
        String token = request.getHeader("token");
        if(StringUtils.isNullOrEmpty(token)){
            response.getWriter().write("你还未登录,请先登录");
            response.setStatus(401);
            return false;
        }

        //！！！！白名单校验
        if(redisUtil.get(TOKEN_PREFIX+token)==null){
            response.getWriter().write("登录凭证已注销，请重新登录");
            response.setStatus(401);
            return false;
        }

        //验证(注意异常的处理和返回友好信息)
        try{
            //验证令牌
            JwtUtil.verifyToken(token);
            //放行请求
            return true;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg","无效签名");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("msg","token过期");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg","token算法不一致");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg","token失效");
        }
        //设置状态
        map.put("state",false);
        response.setStatus(401);
        //将map转化成json，response使用的是Jackson
        String json = new ObjectMapper().writeValueAsString(map);
        response.getWriter().print(json);
        return false;
        //return true;
    }
    /**
     * 目标方法执行完成以后
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    /**
     * 页面渲染以后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}