package com.allianceLogistics.alsys;


import com.allianceLogistics.alsys.common.entity.ErrorMsgVO;
import com.allianceLogistics.alsys.common.service.ErrorMsgService;
import com.allianceLogistics.alsys.config.MyContextListener;
import com.allianceLogistics.alsys.config.MyRequestListener;
import com.allianceLogistics.alsys.config.SessionListener;
import org.mybatis.spring.annotation.MapperScan;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@MapperScan(basePackageClasses = AllianceLogisticsApplication.class)
@SpringBootApplication
public class AllianceLogisticsApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AllianceLogisticsApplication.class);
    }


    public static void main(String[] args) {
        SpringApplication.run(AllianceLogisticsApplication.class, args);

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
    public ServletRequestListener ㄴervletRequestListener() {

        return new MyRequestListener();

    }


    //20220112 정연호 추가. java의 exception을 http로 response 보냄
    @ControllerAdvice
    @RestController
    public class GlobalExceptionHandler {
        @Autowired
        ErrorMsgService errorMsgService;

        @ExceptionHandler(value = Exception.class)

        public ResponseEntity<String> handlerArgumentException(Exception e, HttpSession session) throws UnsupportedEncodingException {

            String result = e.getMessage();
            //result = result.replaceAll("CALL gloakorea_mssql..", "");
			  
			  /*
			  session.getAttribute("userId").toString()
			  session.getAttribute("userNm").toString()
			  session.getAttribute("agntCd").toString()
			  session.getAttribute("agntNm").toString()
			  */
			  /*
			  try {
			  result ="[ "	+	session.getAttribute("tblUserMId").toString()	+	" "
					  		+	session.getAttribute("userId").toString()		+	" "
					  		+ 	session.getAttribute("userNm").toString()		+	" "
					  		+	session.getAttribute("agntCd").toString()		+	" "
							+ 	session.getAttribute("agntNm").toString()		+	" ]" + result;
			  }
			  catch(Exception e1)
			  {
				  result = result;
			  }
			  */
            ErrorMsgVO paramVO = new ErrorMsgVO();
            try {
                paramVO.setCmpyCd(session.getAttribute("cmpyCd").toString());
                paramVO.setTblUserMId(session.getAttribute("tblUserMId").toString());
                paramVO.setSystem(session.getAttribute("system").toString());
            } catch (Exception e2) {
                paramVO.setCmpyCd("");
                paramVO.setTblUserMId("");
                paramVO.setSystem("");
            }
            paramVO.setMsg(result);


            ErrorMsgVO errorMsgVO = errorMsgService.errorSave(paramVO);


            //result=URLEncoder.encode(result,"UTF-8");
            String resultErrorMsg = "오류 추적 번호 : " + errorMsgVO.getId();
            resultErrorMsg = URLEncoder.encode(resultErrorMsg, "UTF-8");
            //ResponseEntity<String> responseEntity = new ResponseEntity<String>(result,HttpStatus.INTERNAL_SERVER_ERROR); // 500 반환
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultErrorMsg, HttpStatus.INTERNAL_SERVER_ERROR); // 500 반환
            return responseEntity;
        }
    }


}


