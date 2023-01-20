package com.gdut.boot.bean;

import com.gdut.boot.constance.common.Code;
import lombok.ToString;

/**
 * @Create 2020-04-20 21:35
 * @Author xm
 * @Description 接口返回数据的封装
 */
@ToString
public class Msg {
    private int code;

    private String message;

    private Object data;

    private Object other;

    public int getCode() {
        return code;
    }

    public Msg setCode(int code) {
        this.code = code;
        return this;
    }

    public Object getOther() {
        return other;
    }

    public Msg setOther(Object other) {
        this.other = other;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Msg setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Msg setData(Object data) {
        this.data = data;
        return this;
    }

    public static Msg success() {
        return success(null, null);
    }

    public static Msg success(String message) {
        return success(message, null);
    }

    public static Msg success(String message, Object data) {
        return new Msg().setCode(Code.SUCCESS_CODE).setMessage(message).setData(data);
    }

    public static Msg success(String message, Object data, Object other) {
        return new Msg().setCode(Code.SUCCESS_CODE).setOther(other).setMessage(message).setData(data);
    }

    public static Msg fail() {
        return fail(null);
    }

    public static Msg fail(String message) {
        return fail(message, null);
    }

    public static Msg UNKNOW() {
        return new Msg().setCode(Code.UNKNOW).setMessage(null).setData(null);
    }

    public static Msg fail(String message, Object data) {
        return new Msg().setCode(Code.FAIL_CODE).setMessage(message).setData(data);
    }

    public static Msg fail(String message, Object data, Object other) {
        return new Msg().setCode(Code.FAIL_CODE).setOther(other).setMessage(message).setData(data);
    }
}
