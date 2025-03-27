package com.sonictms.alsys.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//20220115 정연호. 로그 인터셉터
//@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        try {
            MDC.put("cmpyCd", session.getAttribute("cmpyCd").toString());
            MDC.put("tblUserMId", session.getAttribute("tblUserMId").toString());
            MDC.put("system", session.getAttribute("system").toString());
            MDC.put("cnntSysCd", session.getAttribute("cnntSysCd").toString());
            //MDC.put("userId", session.getAttribute("userId").toString());
            //MDC.put("userNm", session.getAttribute("userNm").toString());
            //MDC.put("agntCd", session.getAttribute("agntCd").toString());
            //MDC.put("agntNm", session.getAttribute("agntNm").toString());
        } catch (Exception e) {

        }
        return true;
    }

    /*
        [PostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)]
        - Controller 진입 후 View가 Rendering 되기 전 수행
        - ModelAndView modelAndView를 통해 화면 단에 들어가는 Data 등의 조작이 가능
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        MDC.clear();
        //log.info("post");
    }

    /*
        [afterComplete(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)]
        - Controller 진입 후 View가 정상적으로 Rendering 된 후 수행
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //log.info("after");
    }
}