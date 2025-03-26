//세션타임아웃 설정
package com.allianceLogistics.alsys.config;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class SessionListener implements HttpSessionListener {

	//sessions라는 Map을 만들어서 키값으로 세션의 고유 생성 아이디값, 값으로는 HttpSession을 갖도록 하였다
	public static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();


    //중복로그인 지우기
    public synchronized static String getSessionidCheck(String type,String compareId){
       String result = "";
       //log.info("getSessionidCheck");
       //log.info("     L>     type : " + type + ", " + "compareId : " + compareId);
        for( String key : sessions.keySet() ){
            HttpSession value = sessions.get(key);
            
            try
            {
	            if(value != null &&  value.getAttribute(type) != null && value.getAttribute(type).toString().equals(compareId) ){
	                result =  key.toString();
	            }
            }
            catch(Exception e)
            {
            	log.error("Session error: " + e.getMessage());
            	sessions.remove(key);	
            }
        }
        //log.info("          L>     result : " + result);
        removeSessionForDoubleLogin(result);
        return result;
    }
    
    private static void removeSessionForDoubleLogin(String sessionId){    	
    	//log.info("removeSessionForDoubleLogin sessionId     : " + sessionId);
        if(sessionId != null && sessionId.length() > 0){
        	//log.info("                      L>     remove sessionId : " + sessionId);
        	sessions.get(sessionId).invalidate();
            sessions.remove(sessionId);    	
            //log.info("                      L>     sessions.size()  : " + SessionListener.sessions.size());	
        }
    }
    //중복로그인 지우기 끝

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    	//log.info("sessionCreated sessionId : " + se.getSession().getId());
    	HttpSession session = se.getSession();
       	session.setAttribute("userName", "JSESSIONID_CREATE");
       	se.getSession().setMaxInactiveInterval(360*60);
        
    	ServletContext application = session.getServletContext();
    	List sessionList = (List) application.getAttribute("sessions");
    	sessionList.add(session);
    }

 

    @Override

    public void sessionDestroyed(HttpSessionEvent se) {
    	log.info("sessionDestroyed sessionChkKey : " + se.getSession().getAttribute("sessionChkKey"));
    	log.info("                 userName      : " + se.getSession().getAttribute("userName"));
    	//log.info("                 userNm        : " + se.getSession().getAttribute("userNm"));
    	log.info("                 sessionId     : " + se.getSession().getId());
		
		//로그아웃이나 브라우저를 종료하여 세션이 사라지게되면 Destroyed에 의해서 제거(세션타임아웃 시간이 된 경우는 if 문에 들어오지 않는다-이미 세션이 없어진 후)
		if(sessions.get(se.getSession().getId()) != null){
			//log.info("sessionDestroyed sessionChkKey : " + se.getSession().getAttribute("sessionChkKey"));
	    	//log.info("                 userNm        : " + se.getSession().getAttribute("userNm"));
	    	//log.info("                 sessionId     : " + se.getSession().getId());
	    	log.info("                      L>     destroyed sessionId : " + se.getSession().getId());
		
        	sessions.get(se.getSession().getId()).invalidate();
            sessions.remove(se.getSession().getId());	
            
        }
		log.info("                      L>     sessions.size()     : " + SessionListener.sessions.size());	
    }

}