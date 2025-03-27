package com.sonictms.alsys.mobile.mNoCmpl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sonictms.alsys.mobile.mNoCmpl.entity.MobileNoCmplVO;
import com.sonictms.alsys.mobile.mNoCmpl.service.MobileNoCmplService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@Transactional
public class MobileNoCmplController {
    
	
	 
	@Autowired
	MobileNoCmplService mobileNoCmplService;

	 //모바일 미마감 onCreate 조회
	  @RequestMapping (value = {"mobile/mNoCmplOnCreate"},method = RequestMethod.POST)
	  @ResponseBody 
	  public MobileNoCmplVO mNoCmplOnCreate(@RequestBody  MobileNoCmplVO mobileNoCmplVO) {
		  mobileNoCmplVO = mobileNoCmplService.mNoCmplOnCreate(mobileNoCmplVO);
		  return mobileNoCmplVO; 
	  }
	  
	  
	  
	//모바일 미마감 처리 결과 저장 SAVE
	  @RequestMapping (value = {"mobile/mNoCmplSaveStat"},method = RequestMethod.POST)
	  @ResponseBody 
	  public MobileNoCmplVO mNoCmplSaveStat(@RequestBody  MobileNoCmplVO mobileNoCmplVO) {
		mobileNoCmplVO = mobileNoCmplService.mNoCmplSaveStat(mobileNoCmplVO);
		
		//20220517 정연호 kpp 오더상태값 변경 때문에 값 저장안할려고 - 테스트만하려고 - 강제롤백 추가함
		//log.info("20220517 정연호 테스트용 저장안하려고 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
		//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	  
		return mobileNoCmplVO; 
	  }
	  
	  
	  
	  
	  
	//모바일 미마감 알림톡 결과 저장 SAVE
	  @RequestMapping (value = {"mobile/mNoCmplSaveTalk"},method = RequestMethod.POST)
	  @ResponseBody 
	  public MobileNoCmplVO mNoCmplSaveTalk(@RequestBody  MobileNoCmplVO mobileNoCmplVO) {
		  mobileNoCmplVO = mobileNoCmplService.mNoCmplSaveTalk(mobileNoCmplVO);
		
		 
		  
		  return mobileNoCmplVO; 
	  }  
	
	  //모바일 미마감 취소처리 처리 저장 Del
	  @RequestMapping (value = {"mobile/mNoCmplDel"},method = RequestMethod.POST)
	  @ResponseBody 
	  public MobileNoCmplVO mNoCmplDel(@RequestBody  MobileNoCmplVO mobileNoCmplVO) {
		  mobileNoCmplVO = mobileNoCmplService.mNoCmplDel(mobileNoCmplVO);
		//20220517 정연호 kpp 오더상태값 변경 때문에 값 저장안할려고 - 테스트만하려고 - 강제롤백 추가함
		//log.info("20220517 정연호 테스트용 저장안하려고 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
		//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	  
		  return mobileNoCmplVO; 
	  }
	  
	  

	  
} 