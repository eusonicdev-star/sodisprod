package com.sonictms.alsys.mobile.mDeliveryDelay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sonictms.alsys.mobile.mDeliveryDelay.entity.MobileDeliveryDelayVO;
import com.sonictms.alsys.mobile.mDeliveryDelay.service.MobileDeliveryDelayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Transactional
public class MobileDeliveryDelayController {

	@Autowired
	MobileDeliveryDelayService mobileDeliveryDelayService;

	@RequestMapping(value = { "mobile/mDeliveryDelayList" }, method = RequestMethod.POST)
	@ResponseBody
	public List<MobileDeliveryDelayVO> mDeliveryDelayList(@RequestBody MobileDeliveryDelayVO mobileDeliveryDelayVO) {
		List<MobileDeliveryDelayVO> list = mobileDeliveryDelayService.mDeliveryDelayList(mobileDeliveryDelayVO);
		return list;
	}

	@RequestMapping(value = { "mobile/mDeliveryDelaySave" }, method = RequestMethod.POST)
	@ResponseBody
	public MobileDeliveryDelayVO mDeliveryDelaySave(@RequestBody MobileDeliveryDelayVO mobileDeliveryDelayVO) {
		mobileDeliveryDelayVO = mobileDeliveryDelayService.mDeliveryDelaySave(mobileDeliveryDelayVO);
		
		//20220517 정연호 kpp 오더상태값 변경 때문에 값 저장안할려고 - 테스트만하려고 - 강제롤백 추가함
		//log.info("20220517 정연호 테스트용 저장안하려고 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
		//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		
		return mobileDeliveryDelayVO;
	}
}