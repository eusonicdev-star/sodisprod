package com.sonictms.alsys.mobile.mLogin.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sonictms.alsys.config.SessionListener;
import com.sonictms.alsys.mobile.mLogin.entity.BaseVO;
import com.sonictms.alsys.mobile.mLogin.entity.MobileLoginVO;
import com.sonictms.alsys.mobile.mLogin.service.MobileLoginService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Transactional
public class MobileLoginController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; // 암호화모듈

	@Autowired
	MobileLoginService mobileLoginService;

	@RequestMapping(value = { "mobile/checkSessionId" }, method = RequestMethod.POST)
	@ResponseBody
	public BaseVO checkSessionId(@RequestBody BaseVO baseVO, HttpServletRequest httpServletRequest) {
		HttpSession session =httpServletRequest.getSession();
		//log.info("어플세션 : " + session.getId());
		//log.info("서버세션 : " + baseVO.getJsessionId());
		if(session.getId().equals(baseVO.getJsessionId())) {
			baseVO.setRtnYn("Y"); 
		}
		else { 
			baseVO.setRtnYn("N"); 
		}
		return baseVO;
	}
	// 모바일 로그인 처리

	@RequestMapping(value = { "mobile/mLogin" }, method = RequestMethod.POST)
	@ResponseBody
	public MobileLoginVO mLogin(@RequestBody MobileLoginVO mobileLoginVO, HttpServletRequest httpServletRequest) {
		//20220220 정연호 syscd 입력을 수정
		MobileLoginVO OriMobileLoginVO = mobileLoginVO;
		
		String cnntIp = mobileLoginVO.getCnntIp();

		// 안드로이드 앱에서 retrofit2 에 의 해 json 으로 전달된걸 서버에서 받을떄 @RequestBody가 있어야 정상적으로 된다.
		// 웹에서는 헤더에 Content-Type: application/json 를 포함시키면 된다
		String inputPw = mobileLoginVO.getUserPw();
		String inputId = mobileLoginVO.getUserId();
		// 입력받은 아이디로 아이디,암호 정보 불러오기

		MobileLoginVO mobileLoginVO2 = mobileLoginVO;
		mobileLoginVO2.setLoginId(inputId);
		mobileLoginVO2 = mobileLoginService.mLogin(mobileLoginVO); // 기존 SP 사용

		// 아이디로 조회된것이 없으면 로그인 실패
		if (mobileLoginVO2 == null) {

			mobileLoginVO.setRtnYn("N");
			mobileLoginVO.setRtnMsg("아이디가 없습니다.");
			return mobileLoginVO;
		}

		String getPw = mobileLoginVO2.getPassword();

		// 입력한 암호와 불러온암호가 불일치
		if (bCryptPasswordEncoder.matches(inputPw, getPw) == false) {

			mobileLoginVO.setRtnYn("N");
			mobileLoginVO.setRtnMsg("아이디 또는 암호를 확인하세요.");
			return mobileLoginVO;
		}

		// 아이디 가지고 사람정보를 불러온다
		MobileLoginVO mobileLoginVO3 = mobileLoginVO;
		mobileLoginVO3.setLoginId(inputId);
		mobileLoginVO3 = mobileLoginService.mUserInfo(mobileLoginVO); // 기존 SP 사용

		if (mobileLoginVO3 == null) {

			mobileLoginVO.setRtnYn("N");
			mobileLoginVO.setRtnMsg("사용자 정보가 조회되지 않습니다.\n관리자에게 문의하세요");
			return mobileLoginVO;
		}

		mobileLoginVO = mobileLoginVO3;
		mobileLoginVO.setUserId(mobileLoginVO.getLoginId());
		mobileLoginVO.setRtnYn("Y");

		// 로그인 히스토리 저장
		//20220220 정연호 syscd 입력을 수정
		//mobileLoginVO.setCnntSysCd("MOBILE");
		mobileLoginVO.setCnntSysCd("M_"+OriMobileLoginVO.getMode().substring(0,2)	+"_"	+OriMobileLoginVO.getVersionName());
		mobileLoginVO.setCnntIp(cnntIp);
		mobileLoginVO.setTblUserMId(mobileLoginVO.getId()+"");	//20220303 정연호 추가
		mobileLoginService.mUserHist(mobileLoginVO); // 기존 SP 사용
		
		// 현재 날짜/시간
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

		mobileLoginVO.setLoginTime(formatDate);
		
		//20220117 정연호 추가 모바일 로그인시 세션관리
		//log.info("MOBILE sessions.size() : " + SessionListener.sessions.size());	

		//회사코드 비교
		log.info("MOBILE 회사코드 비교"); //0505
		log.info("MOBILE      L> cmpyCd : " +mobileLoginVO.getCmpyCd());
		
		log.info("MOBILE sessionChkKey : " + mobileLoginVO.getCmpyCd()+"_mobile_"+mobileLoginVO.getId());	//20220213 정연호 _mobile_ 추가. erp와 구별을 위해
		
		//만들어진 세션을 비교하고 같은게 있으면 세션삭제
		SessionListener.getSessionidCheck("sessionChkKey", mobileLoginVO.getCmpyCd()+"_mobile_"+mobileLoginVO.getId());
		//그 후 그냥 로그인을 진행 (이전 사용자는 튕겨나감)

		HttpSession session =httpServletRequest.getSession();
        
        //session.setAttribute("userIp",cnntIp);	//사용자가 쓰는 피씨의 아이피 가져오기
        session.setAttribute("userId",mobileLoginVO.getLoginId());
		session.setAttribute("userNm",mobileLoginVO.getUserName());
		session.setAttribute("userName",mobileLoginVO.getCmpyCd()+"_mobile_"+mobileLoginVO.getId()+"_"+mobileLoginVO.getUserName());	//20220315 정연호 추가
  		//session.setAttribute("isEnabled","");
		//session.setAttribute("role","");
		//session.setAttribute("useYn",mobileLoginVO.getUseYn());
		session.setAttribute("cmpyCd",mobileLoginVO.getCmpyCd());
		//session.setAttribute("cmpyNm",mobileLoginVO.getCmpyNm());
		
		//ERP권한
		//session.setAttribute("userGrntCd",mobileLoginVO.getUserGrntCd());
		//session.setAttribute("userGrntNm",mobileLoginVO.getUserGrntNm());
		
		//모바일 시스템 권한
		session.setAttribute("mobileGrntCd",mobileLoginVO.getMobileGrntCd());
		session.setAttribute("mobileGrntNm",mobileLoginVO.getMobileGrntNm());
		
		//사용자 구분
		//session.setAttribute("userGrdCd",mobileLoginVO.getUserGrdCd());
		//session.setAttribute("userGrdNm",mobileLoginVO.getUserGrdNm());

		//소속화주
		//session.setAttribute("agntCd",mobileLoginVO.getAgntCd());
		//session.setAttribute("agntNm",mobileLoginVO.getAgntNm());

		//소속지점
		//session.setAttribute("dcCd",mobileLoginVO.getDcCd());
		//session.setAttribute("dcNm",mobileLoginVO.getDcNm());

		session.setAttribute("system","mobile");
		session.setAttribute("tblUserMId",mobileLoginVO.getId());
		session.setAttribute("cnntSysCd",mobileLoginVO.getCnntSysCd());	//20220303 추가 정연호 MO 모바일 lo(local),pr(prod),al(alliance) 10.2.5
		//세션고유구별용 키
		session.setAttribute("sessionChkKey",mobileLoginVO.getCmpyCd()+"_mobile_"+mobileLoginVO.getId());	//20220213 정연호 _mobile_ 추가. erp와 구별을 위해
		
		log.info("MOBILE session - sessionId     : " + session.getId()) ;		//브라우져 쿠키에 있는 세션아이디 JSSEIONID
		//log.info("session.getAttribute(\"userIp\")        : " + session.getAttribute("userIp")) 		;
		log.info("MOBILE session - userId        : " + session.getAttribute("userId")) 		;
		log.info("MOBILE session - userNm        : " + session.getAttribute("userNm")) 		;
		//log.info("session.getAttribute(\"isEnabled\")     : " + session.getAttribute("isEnabled")) 	;
		//log.info("session.getAttribute(\"userRole\")      : " + session.getAttribute("role")) 		;
		//log.info("session.getAttribute(\"useYn\")         : " + session.getAttribute("useYn")) 		;
		log.info("MOBILE session - cmpyCd        : " + session.getAttribute("cmpyCd")) 		;
		//log.info("session.getAttribute(\"cmpyNm\")        : " + session.getAttribute("cmpyNm")) 		;
		//log.info("session.getAttribute(\"userGrntCd\")    : " + session.getAttribute("userGrntCd")) 	; 
		//log.info("session.getAttribute(\"userGrntNm\")    : " + session.getAttribute("userGrntNm")) 	; 
		
		log.info("MOBILE session - mobileGrntCd  : " + session.getAttribute("mobileGrntCd")) 	; 
		log.info("MOBILE session - mobileGrntNm  : " + session.getAttribute("mobileGrntNm")) 	; 
		
		//log.info("session.getAttribute(\"userGrdCd\")     : " + session.getAttribute("userGrdCd")) 	; 
		//log.info("session.getAttribute(\"userGrdNm\")     : " + session.getAttribute("userGrdNm")) 	; 
		//소속화주
		//log.info("session.getAttribute(\"agntCd\")        : " + session.getAttribute("agntCd")) 	; 
		//log.info("session.getAttribute(\"agntNm\")        : " + session.getAttribute("agntNm")) 	; 
		//소속지점
		//log.info("session.getAttribute(\"dcCd\")          : " + session.getAttribute("dcCd")) 	; 
		//log.info("session.getAttribute(\"dcNm\")          : " + session.getAttribute("dcNm")) 	; 

		log.info("MOBILE session - system        : " + session.getAttribute("system")) 	; 
		log.info("MOBILE session - tblUserMId    : " + session.getAttribute("tblUserMId")) 	; 
		log.info("MOBILE session - cnntSysCd     : " + session.getAttribute("cnntSysCd")) 	; 
		log.info("MOBILE session - sessionChkKey : " + session.getAttribute("sessionChkKey")) 	; 

	    SessionListener.sessions.put(session.getId(),session);	//위에서 구한 세션아이디(JSESSIONID) 와 세션정보를 담는다
	    log.info("MOBILE session size() : " + SessionListener.sessions.size());		//현재 살아있는 세션이 뭔지 구한다
		//log.info("MOBILE session - size          : " + SessionListener.sessions.size());	//현재 살아있는 세션이 뭔지 구한다
		mobileLoginVO.setJsessionId(session.getId());	//20220306 정연호 추가
		return mobileLoginVO;
	}
}