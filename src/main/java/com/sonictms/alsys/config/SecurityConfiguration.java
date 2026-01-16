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

    //로그인시 아이디 암호 말고 제 3의 값 받기
    @Autowired
    private CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    //비밀번호 암호화
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return authenticationProvider;
    }

    // invalidateHttpSession(true) 가 정상작동하지 않는 문제
    //로그아웃시 세션이 삭제되지 않기 때문에 로그인 후 로그아웃을 하고 다시 로그인을 하면 에러페이지로 연결이 됩니다.
    //WAS가 하나만 있을 때는 잘 동작하는 것으로 보이겠지만 session clustering 환경에서 로그인 방지 처리가 성공적으로 이뤄지지 않습니다. 
    //session의 추가 혹은 삭제라는 변경사항이 발생하면 모든 WAS로 전파는 되지만 Spring Security까지 전달이 되지 않습니다. 
    //Spring Security가 전달받기 위해서는 아래와 같은 리스너 등록 작업이 필요
    // was가 여러개 있을 때(session clustering)
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

    //Spring-Security 설정으로 로그인 처리와 중복 로그인 방지 처리
    // logout 후 login할 때 정상동작을 위함
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable()
                .addHeaderWriter(
                        new StaticHeadersWriter("X-FRAME-OPTIONS", "SAMEORIGIN")).and()

                .authorizeRequests()// 요청에 의한 보안검사 시작
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
                .anyRequest().authenticated()//어떤 요청에도 보안검사를 한다.
                .and().csrf().disable()
                .formLogin()
                .authenticationDetailsSource(customWebAuthenticationDetailsSource)
                .loginPage("/loginRe")
                .usernameParameter("loginId")
                .passwordParameter("password")
                .loginProcessingUrl("/loginRe")
                .successHandler((httpServletRequest, response, authentication) -> {
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

                    log.info("회사코드 비교");
                    log.info("     L> httpServletRequest cmpyCd : " + httpServletRequest.getParameter("cmpyCd"));
                    log.info("     L> userPrincipal      cmpyCd : " + userPrincipal.getCmpyCd());
                    log.info("     L> sessionChkKey             : " + userPrincipal.getCmpyCd() + "_erp_" + user.getId());

                    if (!httpServletRequest.getParameter("cmpyCd").equals(userPrincipal.getCmpyCd())) {
                        log.info("회사코드 비교 결과 다름");
                        response.sendRedirect("/loginRe?error=cmpyCd");
                        return;
                    } else {
                        log.info("회사코드 비교 결과 같음");
                        log.info("     L> 사용자구분 코드 : " + user.getUserGrdCd());
                        log.info("     L> 사용자구분 명칭 : " + user.getUserGrdNm());
                        if (!user.getUserGrdCd().equals("9000")) {
                            SessionListener.getSessionidCheck("sessionChkKey", userPrincipal.getCmpyCd() + "_erp_" + user.getId());    //20220213 정연호 _erp_ 추가. 모바일과 구별을 위해
                        }
                    }

                    User userH = new User();
                    userH.setTblUserMId(Integer.toString(user.getId()));
                    userH.setCmpyCd(userPrincipal.getCmpyCd());
                    userH.setUserId(userPrincipal.getLoginId());
                    userH.setCnntSysCd("ERP");
                    userH.setCnntIp(etRemoteAddr(httpServletRequest));
                    userH.setSaveUser(userPrincipal.getLoginId());

                    User loginH = new User();
                    try {
                        loginH = userService.loginHistorySave(userH);
                    } catch (Exception e) {
                        response.sendRedirect("/loginRe?error=loginH");
                        return;
                    }

                    HttpSession session = httpServletRequest.getSession();
                    session.setAttribute("userIp", etRemoteAddr(httpServletRequest));
                    session.setAttribute("userId", userPrincipal.getLoginId());
                    session.setAttribute("userNm", userPrincipal.getLoginName());
                    session.setAttribute("userName", user.getCmpyCd() + "_erp_" + user.getId() + "_" + userPrincipal.getLoginName());    //20220315 정연호 추가
                    session.setAttribute("isEnabled", userPrincipal.isEnabled());
                    session.setAttribute("role", userPrincipal.getRole());
                    session.setAttribute("useYn", user.getUseYn());
                    session.setAttribute("cmpyCd", user.getCmpyCd());
                    session.setAttribute("cmpyNm", user.getCmpyNm());

                    //ERP권한
                    session.setAttribute("userGrntCd", user.getUserGrntCd());
                    session.setAttribute("userGrntNm", user.getUserGrntNm());

                    //모바일 시스템 권한
                    session.setAttribute("mobileGrntCd", user.getMobileGrntCd());
                    session.setAttribute("mobileGrntNm", user.getMobileGrntNm());

                    //사용자 구분
                    session.setAttribute("userGrdCd", user.getUserGrdCd());
                    session.setAttribute("userGrdNm", user.getUserGrdNm());

                    //소속화주
                    session.setAttribute("agntCd", user.getAgntCd());
                    session.setAttribute("agntNm", user.getAgntNm());

                    //소속지점
                    session.setAttribute("dcCd", user.getDcCd());
                    session.setAttribute("dcNm", user.getDcNm());

                    session.setAttribute("system", "erp");
                    session.setAttribute("tblUserMId", user.getId());

                    //세션고유구별용 키
                    session.setAttribute("sessionChkKey", user.getCmpyCd() + "_erp_" + user.getId());

                    log.info("Login success - Session ID: " + session.getId());
                    log.info("Login success - User IP: " + session.getAttribute("userIp"));
                    log.info("Login success - User ID: " + session.getAttribute("userId"));
                    log.info("Login success - User Name: " + session.getAttribute("userNm"));
                    log.info("Login success - Company Code: " + session.getAttribute("cmpyCd"));
                    log.info("Login success - Company Name: " + session.getAttribute("cmpyNm"));
                    log.info("session.getId()                       : " + session.getId());        //브라우져 쿠키에 있는 세션아이디 JSSEIONID
                    log.info("session.getAttribute(\"userIp\")        : " + session.getAttribute("userIp"));
                    log.info("session.getAttribute(\"userId\")        : " + session.getAttribute("userId"));
                    log.info("session.getAttribute(\"userNm\")        : " + session.getAttribute("userNm"));
                    log.info("session.getAttribute(\"isEnabled\")     : " + session.getAttribute("isEnabled"));
                    log.info("session.getAttribute(\"userRole\")      : " + session.getAttribute("role"));
                    log.info("session.getAttribute(\"useYn\")         : " + session.getAttribute("useYn"));
                    log.info("session.getAttribute(\"cmpyCd\")        : " + session.getAttribute("cmpyCd"));
                    log.info("session.getAttribute(\"cmpyNm\")        : " + session.getAttribute("cmpyNm"));
                    log.info("session.getAttribute(\"userGrntCd\")    : " + session.getAttribute("userGrntCd"));
                    log.info("session.getAttribute(\"userGrntNm\")    : " + session.getAttribute("userGrntNm"));
                    log.info("session.getAttribute(\"mobileGrntCd\")  : " + session.getAttribute("mobileGrntCd"));
                    log.info("session.getAttribute(\"mobileGrntNm\")  : " + session.getAttribute("mobileGrntNm"));
                    log.info("session.getAttribute(\"userGrdCd\")     : " + session.getAttribute("userGrdCd"));
                    log.info("session.getAttribute(\"userGrdNm\")     : " + session.getAttribute("userGrdNm"));
                    log.info("session.getAttribute(\"agntCd\")        : " + session.getAttribute("agntCd"));
                    log.info("session.getAttribute(\"agntNm\")        : " + session.getAttribute("agntNm"));
                    log.info("session.getAttribute(\"dcCd\")          : " + session.getAttribute("dcCd"));
                    log.info("session.getAttribute(\"dcNm\")          : " + session.getAttribute("dcNm"));
                    log.info("session.getAttribute(\"system\")        : " + session.getAttribute("system"));
                    log.info("session.getAttribute(\"tblUserMId\")    : " + session.getAttribute("tblUserMId"));
                    log.info("session.getAttribute(\"sessionChkKey\") : " + session.getAttribute("sessionChkKey"));

                    SessionListener.sessions.put(session.getId(), session);
                    log.info("create sessions.size() : " + SessionListener.sessions.size());

                    response.sendRedirect("/layout");
                })
                .failureHandler((httpServletRequest, response, e) -> {
                    response.sendRedirect("/loginRe?error=true");
                })
                .permitAll()

                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))    //logout 할때 - 페이지는 ~~~/logout 으로 들어가서 로그아웃을 처리한다
                .addLogoutHandler(new TaskImplementingLogoutHandler())//로그아웃 핸들 - 이곳으로 들어가서 TaskImplementingLogoutHandler.java 안 내용을 처리한다
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
        String ip = null;
        ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}