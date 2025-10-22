package com.sonictms.alsys.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            HttpSession session = request.getSession(false);

            if (session != null) {
                putMdc("cmpyCd", session.getAttribute("cmpyCd"));
                putMdc("tblUserMId", session.getAttribute("tblUserMId"));
                putMdc("system", session.getAttribute("system"));
                putMdc("cnntSysCd", session.getAttribute("cnntSysCd"));
            }
        } catch (IllegalStateException e) {
            log.warn("세션이 이미 무효화됨 - URI: {}", request.getRequestURI(), e);
        } catch (SecurityException e) {
            log.warn("세션 접근 권한 없음 - URI: {}", request.getRequestURI(), e);
        } catch (ClassCastException e) {
            log.warn("세션 속성 타입 캐스팅 오류 - URI: {}", request.getRequestURI(), e);
        } catch (Exception e) {
            log.error("LogInterceptor preHandle 중 예상치 못한 예외 발생 - URI: {}", request.getRequestURI(), e);
            throw e;
        }

        return true;
    }

    private void putMdc(String key, Object value) {
        try {
            if (value != null) {
                MDC.put(key, value.toString());
            }
        } catch (IllegalArgumentException e) {
            log.warn("MDC key가 null이거나 유효하지 않음 - key: {}", key, e);
        } catch (SecurityException e) {
            log.warn("MDC 접근 권한 없음 - key: {}", key, e);
        } catch (Exception e) {
            log.warn("MDC 설정 중 예상치 못한 예외 발생 - key: {}", key, e);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        try {
            MDC.clear();
        } catch (SecurityException e) {
            log.warn("MDC clear 중 접근 권한 없음", e);
        } catch (Exception e) {
            log.error("LogInterceptor postHandle 중 예상치 못한 예외 발생", e);
            throw e;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            MDC.clear();
        } catch (SecurityException e) {
            log.warn("MDC clear 중 접근 권한 없음", e);
        } catch (Exception e) {
            log.error("LogInterceptor afterCompletion 중 예상치 못한 예외 발생", e);
            throw e;
        }
    }
}