package com.gdut.boot.exception;

import com.gdut.boot.bean.Msg;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description: 自定义业务异常类
 * @author: myrdc
 * @date: 0:01 2021/7/26
 */
@Data
public class BusinessException extends RuntimeException {
    private Msg msg;
    private String methodName;
    private Exception e;
    private List<Object> params;
    private Class type;

    public BusinessException(Msg msg, Exception e){
        this.msg = msg;
        this.e = e;
    }

    public BusinessException(Msg msg){
        this.msg = msg;
    }

    public BusinessException(Msg msg, String methodName, Exception e, List<Object> params, Class type) {
        this.msg = msg;
        this.methodName = methodName;
        this.e = e;
        this.params = params;
        this.type = type;
    }
}
