//세션타임아웃 설정
package com.allianceLogistics.alsys.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class MyContextListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent sce) throws RuntimeException {
		ServletContext context = sce.getServletContext();
		ServletContext application = sce.getServletContext();
		// session

		final List sessionList = Collections.synchronizedList(new ArrayList());
		// application
		application.setAttribute("sessions", sessionList);
		//
		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				for (Iterator iterator = sessionList.iterator(); iterator.hasNext();) {
					HttpSession session = (HttpSession) iterator.next();
					try {
						long l = System.currentTimeMillis() - session.getLastAccessedTime(); // ttl
						if (l > (session.getMaxInactiveInterval() * 1000)) {//
							log.info("TTL Check Remove SessionId :  " + session.getAttribute("userName") + "_"
									+ session.getId());
							session.invalidate(); // session
							iterator.remove();
						}
					} catch (Exception e) {
					}
				}
			}
		}, 1800000, 1800000); // 시스템이 시작하고 30분후 부터 30분 단위로 시작
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}