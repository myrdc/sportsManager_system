package com.gdut.boot.exception;
import cn.hutool.core.util.StrUtil;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.vo.res.UserPermissionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


/**
 * @Description: 全局异常统一处理
 * @author: myrdc
 * @date: 0:01 2021/7/26
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionResolver {

    @ExceptionHandler(com.gdut.boot.exception.BusinessException.class)
    @ResponseBody
    public Msg BusinessExceptionHandler(BusinessException e){
        final Exception exception = e.getE();
        log.error("==============================================================");
        log.error("发送异常的方法：" + e.getMethodName());
        log.error("发送异常的参数：" + e.getParams().toString());
        log.error("发送异常的类型：" + e.getType().getName());
        if(exception != null){
            log.error("发送的异常情况：" + exception.getMessage());
            log.error("发送异常的调用栈(展示前几个)：\n");
            final StackTraceElement[] stackTraceElements = Arrays.copyOfRange(exception.getStackTrace(), 0, 10);
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                log.error(stackTraceElement.toString());
            }
        }
        log.error("==============================================================");
        return Msg.fail(e.getMsg().getMessage());
    }


    @ExceptionHandler(com.gdut.boot.exception.AuthException.class)
    @ResponseBody
    public Msg AuthExceptionHandler(AuthException e){
        //异常信息
        final String message = e.getMessage();
        //操作的方法名字
        final String methodName = e.getMethodName();
        //这个方法需要的权限
        final List<Annotation> methodNeed = e.getMethodNeed();
        //操作的用户
        final Object user = e.getUser();
        log.error("==============================================================");
        log.error("权限校验异常的信息：" + message);
        log.error("发生异常的方法：" + methodName);
        log.error("该方法需要的权限：" + methodNeed);
        log.error("用户的信息：" + user);
        log.error("==============================================================");
        return Msg.fail(message);
    }

    @ExceptionHandler(com.gdut.boot.exception.ValidException.class)
    @ResponseBody
    public Msg ValidException(ValidException e){
        //异常信息
        final String message = e.getMessage();
        final Object vo = e.getVo();
        final Set<ConstraintViolation<Object>> set = e.getSet();
        log.error("参数校验异常：" + message);
        log.error("发生异常的类：" + vo);
        List<String> res = new ArrayList<>();
        for (ConstraintViolation<Object> objectConstraintViolation : set) {
            res.add(objectConstraintViolation.getMessage());
        }
        log.error("参数异常的原因：" + res);
        return Msg.fail(StrUtil.join(",", res), null);
    }
}
