package com.sonictms.alsys.common.entity;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

/**
 * 컨트롤러 공통 유틸리티 클래스
 */
@Slf4j
@UtilityClass
public class ControllerUtils {

    /**
     * ModelAndView 생성 및 뷰 이름 설정
     */
    public static ModelAndView createModelAndView(String viewName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    /**
     * ModelAndView 생성 및 뷰 이름, 객체 설정
     */
    public static ModelAndView createModelAndView(String viewName, String objectName, Object object) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(viewName);
        modelAndView.addObject(objectName, object);
        return modelAndView;
    }

    /**
     * API 응답 성공 여부 확인
     */
    public static boolean isSuccessResponse(CommonVO response) {
        return response != null && "Y".equals(response.getRtnYn());
    }

    /**
     * API 응답 실패 여부 확인
     */
    public static boolean isFailureResponse(CommonVO response) {
        return response == null || "N".equals(response.getRtnYn());
    }

    /**
     * 로그 메시지 포맷팅
     */
    public static String formatLogMessage(String operation, String details) {
        return String.format("[%s] %s", operation, details);
    }

    /**
     * 에러 로그 메시지 포맷팅
     */
    public static String formatErrorMessage(String operation, String errorDetails) {
        return String.format("[%s] 오류 발생: %s", operation, errorDetails);
    }
} 