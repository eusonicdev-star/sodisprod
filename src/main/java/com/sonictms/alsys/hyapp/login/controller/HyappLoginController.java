package com.sonictms.alsys.hyapp.login.controller;

import com.sonictms.alsys.hyapp.login.service.HyappLoginService;
// ⛔ jakarta 말고
// import jakarta.servlet.http.Cookie;
// import jakarta.servlet.http.HttpServletResponse;

// ✅ javax로 바꾸기
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

@Controller
public class HyappLoginController {

    private final HyappLoginService loginService;

    public HyappLoginController(HyappLoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/hyapp/userLogin")
    public String hyappUserLogin(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        // 1) (모바일과 동일한 컨셉) 아이디/비번 검증
        boolean success = loginService.login(username, password); // BCrypt matches 기반 :contentReference[oaicite:4]{index=4}
        if (!success) {
            return "redirect:/hyapp/login?error=true";
        }

        // 2) 세션 생성 + 기본 정보 세팅
        HttpSession session = request.getSession(true);

        // HYAPP에서는 현재 repo/service가 PW만 조회하므로 userName/cmpyCd/id 등은 여기서 못 채움
        // (필요하면 HyappUserRepository에 사용자 정보 조회 메소드 추가해서 채우면 됨)
        session.setAttribute("userId", username);
        session.setAttribute("system", "hyapp");

        // 3) (선택) 중복 로그인 방지 - MobileLoginController 패턴
        // cmpyCd, tblUserMId 같은 값이 없어서 일단 username 기반으로 키를 만듦
        // 만약 DB에서 cmpyCd + 사용자 PK를 가져올 수 있으면 아래 키를 ERP/모바일과 동일한 포맷으로 맞추는 걸 추천
        String sessionChkKey = "hyapp_" + username;
        session.setAttribute("sessionChkKey", sessionChkKey);

        try {
            // 프로젝트에 SessionListener가 이미 있으니(모바일에서 사용 :contentReference[oaicite:5]{index=5})
            // 동일하게 호출해서 기존 세션이 있으면 끊도록
            com.sonictms.alsys.config.SessionListener.getSessionidCheck("sessionChkKey", sessionChkKey);
            com.sonictms.alsys.config.SessionListener.sessions.put(session.getId(), session);
        } catch (Exception ignore) {
            // SessionListener가 HYAPP에서 미사용/미구성일 수도 있으니 로그인 자체는 막지 않음
        }

        // 4) 쿠키 세팅 (기존 HyappLoginController의 쿠키 로직 유지하되 HttpOnly는 true 권장 :contentReference[oaicite:6]{index=6})
        Cookie loginCookie = new Cookie("LOGIN_ID", username);
        loginCookie.setPath("/");
        loginCookie.setMaxAge(60 * 60); // 1시간
        loginCookie.setHttpOnly(true);  // ⭐ 보안상 true 권장
        loginCookie.setSecure(request.isSecure());
        response.addCookie(loginCookie);

        Cookie activeCookie = new Cookie("LAST_ACTIVE", String.valueOf(System.currentTimeMillis()));
        activeCookie.setPath("/");
        activeCookie.setMaxAge(60 * 30); // 30분
        activeCookie.setHttpOnly(true);
        activeCookie.setSecure(request.isSecure());
        response.addCookie(activeCookie);

        // 5) 성공 시 메인으로
        return "redirect:/hyapp/main";
    }


    @PostMapping("/hyapp/userLogout")
    public String hyappUserLogout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/hyapp/login";
    }

    @GetMapping("/hyapp/mainPage")
    public String hyappMainpage(@CookieValue(value = "LOGIN_ID", required = false) String loginId) {
        if (loginId == null) {
            return "redirect:/hyapp/login";
        }
        return "redirect:/hyapp/main";
    }

}
