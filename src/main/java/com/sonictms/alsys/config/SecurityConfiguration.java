package com.sonictms.alsys.config;

import com.sonictms.alsys.user.domain.UserPrincipal;
import com.sonictms.alsys.user.entity.User;
import com.sonictms.alsys.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authenticationProvider;
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable()
                .addHeaderWriter(new StaticHeadersWriter("X-FRAME-OPTIONS", "SAMEORIGIN"))
                .and()

                .authorizeRequests()
                .antMatchers("/resources").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/loginRe").permitAll()
                .antMatchers("/repw").permitAll()
                .antMatchers("/repwSendReqNo").permitAll()
                .antMatchers("/repwReqSmsByPhoneNo").permitAll()
                .antMatchers("/changePw").permitAll()
                .antMatchers("/changePwSend").permitAll()
                .antMatchers("/comComboList").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/erpBarcode").permitAll()
                .antMatchers("/mobile/*").permitAll()
                .antMatchers("/robots.txt").permitAll()
                .antMatchers("/sendAlrmTalk").permitAll()
                .antMatchers("/saveAlrmTmpResult").permitAll()
                .antMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/favicon.ico", "/error", "/page/**", "/plugin/**").permitAll()

                // hyapp 로그인 페이지는 공개
                .antMatchers("/hyapp/login").permitAll()

                // hyapp 나머지는 인증 필요
                .antMatchers("/hyapp/**").authenticated()

                .anyRequest().authenticated()
                .and()
                .csrf().disable()

                .formLogin()
                .authenticationDetailsSource(customWebAuthenticationDetailsSource)

                // ✅ 로그인 페이지/처리 URL
                .loginPage("/loginRe")
                .usernameParameter("loginId")
                .passwordParameter("password")
                .loginProcessingUrl("/loginRe")

                .successHandler((httpServletRequest, response, authentication) -> {

                    // ✅ hyapp 여부 (HyappLogin.html에서 system=hyapp로 보내는 전제)
                    String systemParam = httpServletRequest.getParameter("system");
                    boolean isHyapp = "hyapp".equalsIgnoreCase(systemParam);

                    // ✅ 시스템별 키/리다이렉트
                    String sysKey = isHyapp ? "_hyapp_" : "_erp_";
                    String redirectUrl = isHyapp ? "/hyapp/main" : "/layout";
                    String cnntSysCd = isHyapp ? "HYAPP" : "ERP";

                    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                    User user = new User();
                    user.setCmpyCd(userPrincipal.getCmpyCd());
                    user.setLoginId(userPrincipal.getLoginId());

                    try {
                        user = userService.findUserInfoByLoginId(user);
                    } catch (Exception e) {
                        response.sendRedirect("/loginRe?error=userInfo");
                        return;
                    }
                    if (user == null || user.getUserGrntCd() == null || user.getUserGrntCd().isEmpty()) {
                        response.sendRedirect("/loginRe?error=userInfo");
                        return;
                    }

                    // cmpyCd 비교 (null 방지)
                    String reqCmpyCd = httpServletRequest.getParameter("cmpyCd");
                    if (reqCmpyCd == null || !reqCmpyCd.equals(userPrincipal.getCmpyCd())) {
                        response.sendRedirect("/loginRe?error=cmpyCd");
                        return;
                    }

                    // ✅ 중복 로그인 체크 키도 시스템별로 구분
                    if (!"9000".equals(user.getUserGrdCd())) {
                        SessionListener.getSessionidCheck(
                                "sessionChkKey",
                                userPrincipal.getCmpyCd() + sysKey + user.getId()
                        );
                    }

                    // 로그인 히스토리 저장 (시스템별 구분)
                    User userH = new User();
                    userH.setTblUserMId(Integer.toString(user.getId()));
                    userH.setCmpyCd(userPrincipal.getCmpyCd());
                    userH.setUserId(userPrincipal.getLoginId());
                    userH.setCnntSysCd(cnntSysCd);
                    userH.setCnntIp(etRemoteAddr(httpServletRequest));
                    userH.setSaveUser(userPrincipal.getLoginId());

                    try {
                        userService.loginHistorySave(userH);
                    } catch (Exception e) {
                        response.sendRedirect("/loginRe?error=loginH");
                        return;
                    }

                    // 세션 세팅
                    HttpSession session = httpServletRequest.getSession();
                    session.setAttribute("userIp", etRemoteAddr(httpServletRequest));
                    session.setAttribute("userId", userPrincipal.getLoginId());
                    session.setAttribute("userNm", userPrincipal.getLoginName());
                    session.setAttribute("userName", user.getCmpyCd() + sysKey + user.getId() + "_" + userPrincipal.getLoginName());
                    session.setAttribute("isEnabled", userPrincipal.isEnabled());
                    session.setAttribute("role", userPrincipal.getRole());
                    session.setAttribute("useYn", user.getUseYn());
                    session.setAttribute("cmpyCd", user.getCmpyCd());
                    session.setAttribute("cmpyNm", user.getCmpyNm());

                    // ERP 권한(기존 유지)
                    session.setAttribute("userGrntCd", user.getUserGrntCd());
                    session.setAttribute("userGrntNm", user.getUserGrntNm());

                    // 모바일 시스템 권한(기존 유지)
                    session.setAttribute("mobileGrntCd", user.getMobileGrntCd());
                    session.setAttribute("mobileGrntNm", user.getMobileGrntNm());

                    // 사용자 구분
                    session.setAttribute("userGrdCd", user.getUserGrdCd());
                    session.setAttribute("userGrdNm", user.getUserGrdNm());

                    // 소속
                    session.setAttribute("agntCd", user.getAgntCd());
                    session.setAttribute("agntNm", user.getAgntNm());
                    session.setAttribute("dcCd", user.getDcCd());
                    session.setAttribute("dcNm", user.getDcNm());

                    // ✅ system / sessionChkKey 시스템별 분기
                    session.setAttribute("system", isHyapp ? "hyapp" : "erp");
                    session.setAttribute("tblUserMId", user.getId());
                    session.setAttribute("sessionChkKey", user.getCmpyCd() + sysKey + user.getId());

                    SessionListener.sessions.put(session.getId(), session);

                    // ✅ 최종 리다이렉트 (Hyapp이면 /hyapp/main)
                    response.sendRedirect(redirectUrl);
                })

                .failureHandler((httpServletRequest, response, e) -> response.sendRedirect("/loginRe?error=true"))
                .permitAll()

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .addLogoutHandler(new TaskImplementingLogoutHandler())
                .permitAll()
                .logoutSuccessUrl("/loginRe")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new AjaxAuthenticationEntryPoint("/loginRe"))

                .and()
                .sessionManagement();
    }

    public static String etRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("X-RealIP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("REMOTE_ADDR");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ip = request.getRemoteAddr();
        return ip;
    }
}
