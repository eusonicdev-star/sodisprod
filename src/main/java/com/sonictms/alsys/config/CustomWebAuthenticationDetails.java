package com.sonictms.alsys.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = 1L;
	private final String cmpyCd;

   public CustomWebAuthenticationDetails(HttpServletRequest request) {
       super(request);
       // 로그인 폼에서 선언한 파라미터 명으로 request
       cmpyCd = request.getParameter("cmpyCd");
   }

   public String getCmpyCd() {
       return cmpyCd;
   }
}