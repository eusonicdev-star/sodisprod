package com.sonictms.alsys.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

@Getter
@Slf4j
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = 1L;
	private final String cmpyCd;

   public CustomWebAuthenticationDetails(HttpServletRequest request) {
       super(request);
       cmpyCd = request.getParameter("cmpyCd");
   }
}