package com.allianceLogistics.alsys.config;

//spring boot 보안 설정

import com.allianceLogistics.alsys.user.domain.UserPrincipal;
import com.allianceLogistics.alsys.user.entity.User;
import com.allianceLogistics.alsys.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
        //log.info("ServletListenerRegistrationBean");
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

    //Spring-Security 설정으로 로그인 처리와 중복 로그인 방지 처리
    // logout 후 login할 때 정상동작을 위함
    @Bean
    public SessionRegistry sessionRegistry() {
        log.info("SessionRegistry");
        return new SessionRegistryImpl();
    }

    /*
     * @Override protected void configure(AuthenticationManagerBuilder auth) {
     * auth.authenticationProvider(authenticationProvider(userService)); }
     */

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

                //20210927 정연호 추가
                .antMatchers("/erpBarcode").permitAll()
                //20210929 정연호 추가
                .antMatchers("/mobile/*").permitAll()
                //.antMatchers("/mobile/mLogin").permitAll()
                //20211110 정연호 추가
                .antMatchers("/robots.txt").permitAll()

                //20220222 정연호 추가. 알림톡 보내기
                .antMatchers("/sendAlrmTalk").permitAll()
                //20220222 정연호 추가. 알림톡 발송 결과를 DB에 저장하기
                .antMatchers("/saveAlrmTmpResult").permitAll()


                //20220617 정연호 web.ignore 의 warm 해결을 위해 여기에 적음
                .antMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/favicon.ico", "/error", "/page/**", "/plugin/**").permitAll()


                //.antMatchers("/main").hasAuthority("ADMIN") // ADMIN 권한의 유저만 /home 에 접근가능
                .anyRequest().authenticated()//어떤 요청에도 보안검사를 한다.
                .and().csrf().disable()

                .formLogin()//보안 검증은 formLogin방식으로 하겠다.
                // 사용자 정의 AuthenticationDetailsSource 추가
                .authenticationDetailsSource(customWebAuthenticationDetailsSource)

                .loginPage("/loginRe")//사용자 정의 로그인 페이지. /loginRe 로 들어오는게 스프링 시큐리티로 처리할 로그인이다 

                .usernameParameter("loginId")//아이디 파라미터명 설정
                .passwordParameter("password")//패스워드 파라미터명 설정
                .loginProcessingUrl("/loginRe")//로그인 Form Action Url

                //.successHandler(loginSuccessHandler())//로그인 성공 후 핸들러 (해당 핸들러를 생성하여 핸들링 해준다.)
                //.failureHandler(loginFailureHandler())//로그인 실패 후 핸들러 (해당 핸들러를 생성하여 핸들링 해준다.)

                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                                        HttpServletResponse response,

                                                        Authentication authentication) throws IOException, ServletException {

                        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

                        //로그인 한 후에 회사코드와 아이디로 사용자 정보 찾아오기
                        User user = new User();
                        user.setCmpyCd(userPrincipal.getCmpyCd());
                        user.setLoginId(userPrincipal.getLoginId());

                        try {
                            user = userService.findUserInfoByLoginId(user);
                        } catch (Exception e) {
                            //e.printStackTrace();
                            response.sendRedirect("/loginRe?error=userInfo");    //로그인 에러시 페이지 이동
                            return;
                        }

                        //사용자 아이디로 사용자 정보를 찾을수 없거나 ERP시스템 권한이 없으면 에러페이지로 이동
                        if (user == null || user.getUserGrntCd() == null || user.getUserGrntCd().equals("")) {
                            response.sendRedirect("/loginRe?error=userInfo");    //로그인 에러시 페이지 이동
                            return;
                        }

                        //세션리스너 가서 로그인 한 해당 아이디로 먼저 세션에 있는지 찾아서 만약있으면 그 세션 지워버림 (기존에 로그인되었던 사용자는 이제 뭔가 하려하면 튕기고 로그인하라고 나옴)

                        //log.info("before create sessions.size() : " + SessionListener.sessions.size());
                        //회사코드 비교
                        log.info("회사코드 비교"); //0505
                        log.info("     L> httpServletRequest cmpyCd : " + httpServletRequest.getParameter("cmpyCd")); //0505
                        log.info("     L> userPrincipal      cmpyCd : " + userPrincipal.getCmpyCd()); //0505
                        log.info("     L> sessionChkKey             : " + userPrincipal.getCmpyCd() + "_erp_" + user.getId()); //0505//20220213 정연호 _erp_ 추가. 모바일과 구별을 위해

                        //로그인 화면에서 선택한 그 회사코드와 로그인 사용자 아이디로 검색한 그 회사코드가 같으면 (같은회사면) 기존에 사용자 ID 에
                        if (!httpServletRequest.getParameter("cmpyCd").equals(userPrincipal.getCmpyCd())) {
                            log.info("회사코드 비교 결과 다름"); //0505
                            response.sendRedirect("/loginRe?error=cmpyCd");    //로그인 에러시 페이지 이동
                            return;
                        } else {
                            log.info("회사코드 비교 결과 같음"); //0505
                            log.info("     L> 사용자구분 코드 : " + user.getUserGrdCd()); //0505
                            log.info("     L> 사용자구분 명칭 : " + user.getUserGrdNm()); //0505
                            if (!user.getUserGrdCd().equals("9000")) {
                                //만들어진 세션을 비교하고 같은게 있으면 세션삭제
                                SessionListener.getSessionidCheck("sessionChkKey", userPrincipal.getCmpyCd() + "_erp_" + user.getId());    //20220213 정연호 _erp_ 추가. 모바일과 구별을 위해
                                //그 후 그냥 로그인을 진행 (이전 사용자는 튕겨나감)
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
                            //e.printStackTrace();
                            response.sendRedirect("/loginRe?error=loginH");    //로그인 에러시 페이지 이동
                            return;
                        }

                        HttpSession session = httpServletRequest.getSession();

                        session.setAttribute("userIp", etRemoteAddr(httpServletRequest));    //사용자가 쓰는 피씨의 아이피 가져오기
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
                        //소속화주
                        log.info("session.getAttribute(\"agntCd\")        : " + session.getAttribute("agntCd"));
                        log.info("session.getAttribute(\"agntNm\")        : " + session.getAttribute("agntNm"));
                        //소속지점
                        log.info("session.getAttribute(\"dcCd\")          : " + session.getAttribute("dcCd"));
                        log.info("session.getAttribute(\"dcNm\")          : " + session.getAttribute("dcNm"));

                        log.info("session.getAttribute(\"system\")        : " + session.getAttribute("system"));
                        log.info("session.getAttribute(\"tblUserMId\")    : " + session.getAttribute("tblUserMId"));

                        log.info("session.getAttribute(\"sessionChkKey\") : " + session.getAttribute("sessionChkKey"));


                        SessionListener.sessions.put(session.getId(), session);    //위에서 구한 세션아이디(JSESSIONID) 와 세션정보를 담는다
                        log.info("create sessions.size() : " + SessionListener.sessions.size());        //현재 살아있는 세션이 뭔지 구한다

                        response.sendRedirect("/layout");    //로그인 성공시 페이지 이동
                    }
                })//로그인 성공 후 핸들러

                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                                        HttpServletResponse response,
                                                        AuthenticationException e) throws IOException, ServletException {
                        log.info("failureHandler cmpyCd : " + httpServletRequest.getParameter("cmpyCd"));
                        response.sendRedirect("/loginRe?error=true");
                    }
                })//로그인 실패 후 핸들러

                .permitAll() //사용자 정의 로그인 페이지 접근 권한 승인

                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))    //logout 할때 - 페이지는 ~~~/logout 으로 들어가서 로그아웃을 처리한다
                .addLogoutHandler(new TaskImplementingLogoutHandler())//로그아웃 핸들 - 이곳으로 들어가서 TaskImplementingLogoutHandler.java 안 내용을 처리한다
                .permitAll()
                .logoutSuccessUrl("/loginRe")    //로그아웃 성공시 이동할 페이지
                .clearAuthentication(true)        /*로그아웃시 세션 제거*/
                .invalidateHttpSession(true)    /*권한정보 제거*/
                .deleteCookies("JSESSIONID")    /*쿠키 제거*/

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new AjaxAuthenticationEntryPoint("/loginRe"))

                .and()
                .sessionManagement()

        ;
    }

    @Override
    public void configure(WebSecurity web) {
        //web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).antMatchers("/css/**", "/js/**", "/img/**","/favicon.ico","/error","/page/**","/plugin/**");

    }

    public static String etRemoteAddr(HttpServletRequest request) {

        String ip = null;
        ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}