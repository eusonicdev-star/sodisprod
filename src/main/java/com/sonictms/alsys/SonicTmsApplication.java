package com.sonictms.alsys;

import com.sonictms.alsys.common.entity.ErrorMsgVO;
import com.sonictms.alsys.common.service.ErrorMsgService;
import com.sonictms.alsys.config.MyContextListener;
import com.sonictms.alsys.config.MyRequestListener;
import com.sonictms.alsys.config.SessionListener;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;

@MapperScan(basePackageClasses = SonicTmsApplication.class)
@SpringBootApplication
public class SonicTmsApplication extends SpringBootServletInitializer {
    private static final Logger log = LoggerFactory.getLogger(SonicTmsApplication.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SonicTmsApplication.class);
    }


    public static void main(String[] args) {
        SpringApplication.run(SonicTmsApplication.class, args);

    }

    //세션타임아웃 설정하기
    @Bean
    public HttpSessionListener httpSessionListener() {
        return new SessionListener();
    }

    //20220315 정연호 추가 서블릿 리스너 추가-. 애플리케이션 실행,종료 알수있음
    @Bean
    public ServletContextListener servletContextListener() {
        return new MyContextListener();
    }

    //20220315 정연호 추가 리퀘스트 리스너 추가-. 요청정보 생성,종료 알수있음
    @Bean
    public ServletRequestListener servletRequestListener() {
        return new MyRequestListener();
    }

    // 20220112 정연호 추가. java의 exception을 http로 response 보냄
    @ControllerAdvice
    @RestController
    public class GlobalExceptionHandler {

        @Autowired
        ErrorMsgService errorMsgService;

        // NullPointerException 처리
        @ExceptionHandler(value = NullPointerException.class)
        public ResponseEntity<String> handleNullPointerException(NullPointerException e, HttpSession session) throws UnsupportedEncodingException {
            String result = "NullPointerException: " + e.getMessage();
            log.error("NullPointerException 발생: {}", e.getMessage(), e);
            return createErrorResponse(result, session, "NullPointerException");
        }

        // IllegalArgumentException 처리
        @ExceptionHandler(value = IllegalArgumentException.class)
        public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e, HttpSession session) throws UnsupportedEncodingException {
            String result = "IllegalArgumentException: " + e.getMessage();
            log.error("IllegalArgumentException 발생: {}", e.getMessage(), e);
            return createErrorResponse(result, session, "IllegalArgumentException");
        }

        // SQLException 처리
        @ExceptionHandler(value = SQLException.class)
        public ResponseEntity<String> handleSQLException(SQLException e, HttpSession session) throws UnsupportedEncodingException {
            String result = "SQLException: " + e.getMessage();
            log.error("SQLException 발생: {}", e.getMessage(), e);
            return createErrorResponse(result, session, "SQLException");
        }

        // IOException 처리
        @ExceptionHandler(value = IOException.class)
        public ResponseEntity<String> handleIOException(IOException e, HttpSession session) throws UnsupportedEncodingException {
            String result = "IOException: " + e.getMessage();
            log.error("IOException 발생: {}", e.getMessage(), e);
            return createErrorResponse(result, session, "IOException");
        }

        // RuntimeException 처리
        @ExceptionHandler(value = RuntimeException.class)
        public ResponseEntity<String> handleRuntimeException(RuntimeException e, HttpSession session) throws UnsupportedEncodingException {
            String result = "RuntimeException: " + e.getMessage();
            log.error("RuntimeException 발생: {}", e.getMessage(), e);
            return createErrorResponse(result, session, "RuntimeException");
        }

        // 기타 모든 Exception 처리
        @ExceptionHandler(value = Exception.class)
        public ResponseEntity<String> handleException(Exception e, HttpSession session) throws UnsupportedEncodingException {
            String result = "Exception: " + e.getMessage();
            log.error("Exception 발생: {}", e.getMessage(), e);
            return createErrorResponse(result, session, "Exception");
        }

        // 공통 에러 응답 생성 메서드
        private ResponseEntity<String> createErrorResponse(String result, HttpSession session, String exceptionType) throws UnsupportedEncodingException {
            ErrorMsgVO paramVO = new ErrorMsgVO();
            try {
                paramVO.setCmpyCd(session.getAttribute("cmpyCd") != null ? session.getAttribute("cmpyCd").toString() : "");
                paramVO.setTblUserMId(session.getAttribute("tblUserMId") != null ? session.getAttribute("tblUserMId").toString() : "");
                paramVO.setSystem(session.getAttribute("system") != null ? session.getAttribute("system").toString() : "");
            } catch (IllegalStateException e) {
                log.warn("세션이 이미 무효화됨: {}", e.getMessage());
                paramVO.setCmpyCd("");
                paramVO.setTblUserMId("");
                paramVO.setSystem("");
            } catch (ClassCastException e) {
                log.warn("세션 속성 타입 캐스팅 오류: {}", e.getMessage());
                paramVO.setCmpyCd("");
                paramVO.setTblUserMId("");
                paramVO.setSystem("");
            } catch (SecurityException e) {
                log.warn("세션 접근 권한 없음: {}", e.getMessage());
                paramVO.setCmpyCd("");
                paramVO.setTblUserMId("");
                paramVO.setSystem("");
            } catch (Exception e) {
                log.warn("세션 정보 추출 중 예상치 못한 오류: {}", e.getMessage());
                paramVO.setCmpyCd("");
                paramVO.setTblUserMId("");
                paramVO.setSystem("");
            }
            paramVO.setMsg(result);
            ErrorMsgVO errorMsgVO = errorMsgService.errorSave(paramVO);
            String resultErrorMsg = "오류 추적 번호 : " + errorMsgVO.getId() + " (예외타입: " + exceptionType + ")";
            resultErrorMsg = URLEncoder.encode(resultErrorMsg, "UTF-8");
            return new ResponseEntity<String>(resultErrorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
