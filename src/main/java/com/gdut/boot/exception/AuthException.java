package com.gdut.boot.exception;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.vo.res.UserPermissionVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/5/2416:59
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthException extends RuntimeException{

    private String message;
    private String methodName;
    private List<Annotation> methodNeed;
    private Object user;

}
