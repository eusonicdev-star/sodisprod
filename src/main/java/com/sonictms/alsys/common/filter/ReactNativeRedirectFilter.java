package com.sonictms.alsys.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReactNativeRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String userAgent = req.getHeader("User-Agent");

        boolean isReactNative =
                userAgent != null &&
                        userAgent.toLowerCase().contains("react-native");

        boolean isLoginPage = uri.equals("/login") || uri.equals("/loginRe");

        if (isReactNative && isLoginPage) {
            res.sendRedirect("/hyapp/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
