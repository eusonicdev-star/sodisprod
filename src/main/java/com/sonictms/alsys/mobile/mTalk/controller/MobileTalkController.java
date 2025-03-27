package com.sonictms.alsys.mobile.mTalk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sonictms.alsys.mobile.mTalk.entity.MobileTalkVO;
import com.sonictms.alsys.mobile.mTalk.service.MobileTalkService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@Transactional	//트랜잭션 오류시 롤백
public class MobileTalkController {
	 
	@Autowired
	MobileTalkService mobileTalkService;	  
	  
	//모바일 알림톡 전송할 내용 찾기
	  @RequestMapping (value = {"mobile/mSrchTalk"},method = RequestMethod.POST)
	  @ResponseBody 
	  public MobileTalkVO mSrchTalk(@RequestBody  MobileTalkVO mobileTalkVO) {
		  mobileTalkVO= mobileTalkService.mSrchTalk(mobileTalkVO);
		  return mobileTalkVO; 
	  }
	//모바일 알림톡 발송결과를 DB에 저장
	  @RequestMapping (value = {"mobile/mSaveTalk"},method = RequestMethod.POST)
	  @ResponseBody 
	 
	  public MobileTalkVO mSaveTalk(@RequestBody  MobileTalkVO mobileTalkVO) {
		  mobileTalkVO= mobileTalkService.mSaveTalk(mobileTalkVO);
		  return mobileTalkVO; 
	  } 

	//모바일 해피콜완료 4000 => 5000 으로 바꾸기
	  @RequestMapping (value = {"mobile/mSaveStat"},method = RequestMethod.POST)
	  @ResponseBody 
	
	  public MobileTalkVO mSaveStat(@RequestBody  MobileTalkVO mobileTalkVO) {
		
		  mobileTalkVO= mobileTalkService.mSaveStat(mobileTalkVO);
		
		//20220517 정연호 kpp 오더상태값 변경 때문에 값 저장안할려고 - 테스트만하려고 - 강제롤백 추가함
		//log.info("20220517 정연호 테스트용 저장안하려고 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
		//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	
		  return mobileTalkVO; 
	  } 	
} 