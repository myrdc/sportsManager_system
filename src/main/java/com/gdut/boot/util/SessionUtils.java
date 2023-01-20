package com.gdut.boot.util;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @description:
 * @author: HP
 * @date: 2020-05-12 12:15
 */
public class SessionUtils {

    private static ThreadLocal<HttpSession> sessionThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<>();

    public static void setSession(HttpSession session){
        sessionThreadLocal.set(session);
    }

    public static HttpSession getSession(){
        return sessionThreadLocal.get();
    }

    public static void removeSession(){
        sessionThreadLocal.remove();
    }

    public static void setResponse(HttpServletResponse session){
        responseThreadLocal.set(session);
    }

    public static HttpServletResponse getResponse(){
        return responseThreadLocal.get();
    }

    public static void removeResponse(){
        responseThreadLocal.remove();
    }

}
