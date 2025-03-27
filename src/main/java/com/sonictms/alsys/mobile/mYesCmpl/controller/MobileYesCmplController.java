package com.sonictms.alsys.mobile.mYesCmpl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sonictms.alsys.mobile.mYesCmpl.entity.MobileYesCmplVO;
import com.sonictms.alsys.mobile.mYesCmpl.service.MobileYesCmplService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Transactional
public class MobileYesCmplController {

	@Autowired
	MobileYesCmplService mobileYesCmplService;

	// 모바일 배송완료 onCreate 조회
	@RequestMapping(value = { "mobile/mYesCmplOnCreate" }, method = RequestMethod.POST)
	@ResponseBody
	public MobileYesCmplVO mYesCmplOnCreate(@RequestBody MobileYesCmplVO mobileYesCmplVO) {
		mobileYesCmplVO = mobileYesCmplService.mYesCmplOnCreate(mobileYesCmplVO);
		return mobileYesCmplVO;
	}

	// 모바일 배송완료 처리 저장 SAVE
	@RequestMapping(value = { "mobile/mYesCmplSaveStat" }, method = RequestMethod.POST)
	@ResponseBody
	public MobileYesCmplVO mYesCmplSaveStat(@RequestBody MobileYesCmplVO mobileYesCmplVO) {

		mobileYesCmplVO = mobileYesCmplService.mYesCmplSaveStat(mobileYesCmplVO);
		
		//20220517 정연호 kpp 오더상태값 변경 때문에 값 저장안할려고 - 테스트만하려고 - 강제롤백 추가함
		//log.info("20220517 정연호 테스트용 저장안하려고 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
		//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		return mobileYesCmplVO;
	}

	// 모바일 배송완료 취소처리 처리 저장 Del
	@RequestMapping(value = { "mobile/mYesCmplDel" }, method = RequestMethod.POST)
	@ResponseBody
	public MobileYesCmplVO mYesCmplDel(@RequestBody MobileYesCmplVO mobileYesCmplVO) {
		mobileYesCmplVO = mobileYesCmplService.mYesCmplDel(mobileYesCmplVO);
		
		//20220517 정연호 kpp 오더상태값 변경 때문에 값 저장안할려고 - 테스트만하려고 - 강제롤백 추가함
		//log.info("20220517 정연호 테스트용 저장안하려고 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
		//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		
		return mobileYesCmplVO;
	}

	// 모바일 배송완료 알림톡 결과 저장
	@RequestMapping(value = { "mobile/mYesCmplSaveTalk" }, method = RequestMethod.POST)
	@ResponseBody
	public MobileYesCmplVO mYesCmplSaveTalk(@RequestBody MobileYesCmplVO mobileYesCmplVO) {

		mobileYesCmplVO = mobileYesCmplService.mYesCmplSaveTalk(mobileYesCmplVO);

		return mobileYesCmplVO;
	}

}