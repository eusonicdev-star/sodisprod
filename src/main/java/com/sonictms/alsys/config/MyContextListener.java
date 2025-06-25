package com.sonictms.alsys.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MyContextListener implements ServletContextListener {
	
	private static final long SESSION_CHECK_INTERVAL = TimeUnit.MINUTES.toMillis(30); // 30분
	private static final long INITIAL_DELAY = TimeUnit.MINUTES.toMillis(30); // 초기 지연 30분
	private Timer sessionTimer;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			ServletContext context = sce.getServletContext();
			
			final List<HttpSession> sessionList = Collections.synchronizedList(new ArrayList<>());
			context.setAttribute("sessions", sessionList);
			
			sessionTimer = new Timer("SessionCleanupTimer", true);
			sessionTimer.schedule(new SessionCleanupTask(sessionList), INITIAL_DELAY, SESSION_CHECK_INTERVAL);
			
			log.info("MyContextListener 초기화 완료 - 세션 정리 작업이 {}분 후부터 {}분 간격으로 실행됩니다.", 
					TimeUnit.MILLISECONDS.toMinutes(INITIAL_DELAY),
					TimeUnit.MILLISECONDS.toMinutes(SESSION_CHECK_INTERVAL));
					
		} catch (IllegalStateException e) {
			log.error("ServletContext가 이미 초기화되었거나 파괴됨", e);
			throw new RuntimeException("MyContextListener 초기화 실패 - ServletContext 상태 오류", e);
		} catch (SecurityException e) {
			log.error("ServletContext 접근 권한 없음", e);
			throw new RuntimeException("MyContextListener 초기화 실패 - 접근 권한 오류", e);
		} catch (Exception e) {
			log.error("MyContextListener 초기화 중 예상치 못한 예외 발생", e);
			throw new RuntimeException("MyContextListener 초기화 실패", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			if (sessionTimer != null) {
				sessionTimer.cancel();
				sessionTimer = null;
				log.info("MyContextListener 정리 완료 - 세션 정리 타이머가 중지되었습니다.");
			}
		} catch (IllegalStateException e) {
			log.warn("타이머가 이미 취소됨", e);
		} catch (SecurityException e) {
			log.warn("타이머 취소 권한 없음", e);
		} catch (Exception e) {
			log.error("MyContextListener 정리 중 예상치 못한 예외 발생", e);
		}
	}

	private static class SessionCleanupTask extends TimerTask {
		private final List<HttpSession> sessionList;
		
		public SessionCleanupTask(List<HttpSession> sessionList) {
			this.sessionList = sessionList;
		}
		
		@Override
		public void run() {
			try {
				cleanupExpiredSessions();
			} catch (IllegalStateException e) {
				log.error("세션 정리 작업 중 타이머 상태 오류", e);
			} catch (SecurityException e) {
				log.error("세션 정리 작업 중 접근 권한 오류", e);
			} catch (Exception e) {
				log.error("세션 정리 작업 중 예상치 못한 예외 발생", e);
			}
		}
		
		private void cleanupExpiredSessions() {
			synchronized (sessionList) {
				Iterator<HttpSession> iterator = sessionList.iterator();
				int removedCount = 0;
				
				while (iterator.hasNext()) {
					HttpSession session = iterator.next();
					try {
						if (isSessionExpired(session)) {
							String userName = getSessionUserName(session);
							String sessionId = session.getId();
							
							log.info("세션 만료로 제거 - 사용자: {}, 세션ID: {}", userName, sessionId);
							
							session.invalidate();
							iterator.remove();
							removedCount++;
						}
					} catch (IllegalStateException e) {
						log.warn("세션이 이미 무효화됨 - 세션ID: {}", session.getId(), e);
						iterator.remove();
						removedCount++;
					} catch (SecurityException e) {
						log.warn("세션 접근 권한 없음 - 세션ID: {}", session.getId(), e);
						iterator.remove();
						removedCount++;
					} catch (Exception e) {
						log.warn("세션 처리 중 예상치 못한 예외 발생 - 세션ID: {}", session.getId(), e);
						iterator.remove();
						removedCount++;
					}
				}
				
				if (removedCount > 0) {
					log.info("세션 정리 완료 - 제거된 세션 수: {}, 남은 세션 수: {}", removedCount, sessionList.size());
				}
			}
		}
		
		private boolean isSessionExpired(HttpSession session) {
			try {
				long currentTime = System.currentTimeMillis();
				long lastAccessedTime = session.getLastAccessedTime();
				long maxInactiveInterval = session.getMaxInactiveInterval() * 1000L;
				
				return (currentTime - lastAccessedTime) > maxInactiveInterval;
			} catch (IllegalStateException e) {
				log.warn("세션이 이미 무효화되어 만료 확인 불가 - 세션ID: {}", session.getId(), e);
				return true;
			} catch (SecurityException e) {
				log.warn("세션 접근 권한 없어 만료 확인 불가 - 세션ID: {}", session.getId(), e);
				return true;
			} catch (Exception e) {
				log.warn("세션 만료 확인 중 예상치 못한 예외 발생 - 세션ID: {}", session.getId(), e);
				return true;
			}
		}
		
		private String getSessionUserName(HttpSession session) {
			try {
				Object userName = session.getAttribute("userName");
				return userName != null ? userName.toString() : "unknown";
			} catch (IllegalStateException e) {
				log.warn("세션이 이미 무효화되어 사용자명 조회 불가 - 세션ID: {}", session.getId(), e);
				return "unknown";
			} catch (SecurityException e) {
				log.warn("세션 접근 권한 없어 사용자명 조회 불가 - 세션ID: {}", session.getId(), e);
				return "unknown";
			} catch (ClassCastException e) {
				log.warn("사용자명 속성 타입 캐스팅 오류 - 세션ID: {}", session.getId(), e);
				return "unknown";
			} catch (Exception e) {
				log.warn("세션 사용자명 조회 중 예상치 못한 예외 발생 - 세션ID: {}", session.getId(), e);
				return "unknown";
			}
		}
	}
}