package com.sonictms.alsys.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@WebListener
public class SessionListener implements HttpSessionListener {

	//sessions라는 Map을 만들어서 키값으로 세션의 고유 생성 아이디값, 값으로는 HttpSession을 갖도록 하였다
	public static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    //중복 로그인 지우기
    public synchronized static String getSessionidCheck(String type, String compareId) {
       String result = "";
        for (String key : sessions.keySet()) {
            HttpSession value = sessions.get(key);

            try {
                if (value != null && value.getAttribute(type).toString().equals(compareId)) {
                    result = key;
	            }
            } catch (Exception e) {
            	log.error("Session error: " + e.getMessage());
            	sessions.remove(key);	
            }
        }

        removeSessionForDoubleLogin(result);
        return result;
    }

    private static void removeSessionForDoubleLogin(String sessionId) {
        if (sessionId != null && !sessionId.isEmpty()) {
        	sessions.get(sessionId).invalidate();
            sessions.remove(sessionId);    	
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        session.setMaxInactiveInterval(21600);
        session.setAttribute("userName", "JSESSIONID_CREATE");

        sessions.put(session.getId(), session);

        ServletContext application = session.getServletContext();
        synchronized (application) {
            List<HttpSession> sessionList = (List<HttpSession>) application.getAttribute("sessions");
            if (sessionList == null) {
                sessionList = new CopyOnWriteArrayList<>();
                application.setAttribute("sessions", sessionList);
            }
            sessionList.add(session);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        sessions.remove(se.getSession().getId());
    }
}
