package com.gdut.boot.filter;
import com.gdut.boot.util.SessionUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:
 * @author: HP
 * @date: 2020-06-25 15:33
 */

@WebFilter(filterName="sessionInjectionFilter", urlPatterns="/*")
public class SessionInjectionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SessionUtils.setSession(null);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req =(HttpServletRequest) request;

        HttpServletResponse res =(HttpServletResponse) response;

        //当前线程绑定HttpSession
        //多线程下防止线程串线
        SessionUtils.setSession(req.getSession(true));
        SessionUtils.setResponse(res);

        chain.doFilter(request, response);

        //用完就去掉
        SessionUtils.removeSession();
        SessionUtils.setSession(null);
        SessionUtils.removeResponse();
        SessionUtils.setResponse(null);
    }

    @Override
    public void destroy() {
        SessionUtils.removeResponse();
        SessionUtils.removeSession();
    }
}
