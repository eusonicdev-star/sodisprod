package com.allianceLogistics.alsys.mobile.mTalk.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allianceLogistics.alsys.common.entity.ErpCommonVO;
import com.allianceLogistics.alsys.common.mapper.ErpCommonMapper;
import com.allianceLogistics.alsys.common.service.ErpCommonService;
import com.allianceLogistics.alsys.mobile.mTalk.entity.MobileTalkVO;
import com.allianceLogistics.alsys.mobile.mTalk.mapper.MobileTalkMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class MobileTalkService  {
	 
	@Autowired
	private  MobileTalkMapper mobileTalkMapper;

	//20220517 정연호 kpp오더 상태값 변경하기위해서 추가함
	@Autowired
	private ErpCommonMapper erpCommonMapper;
	//20220517 정연호 kpp오더 상태값 변경하기위해서 추가함
	@Autowired
	private ErpCommonService erpCommonService;
	
	//모바일 알림톡 불러오기 - 알림톡 보내기 전에 불러오기
	public MobileTalkVO mSrchTalk(MobileTalkVO mobileTalkVO) { 
		mobileTalkVO = mobileTalkMapper.mSrchTalk(mobileTalkVO);
		return mobileTalkVO; 
	}	

	//모바일 알림톡 발송결과를 DB에 저장
	public MobileTalkVO mSaveTalk(MobileTalkVO mobileTalkVO) { 
		mobileTalkVO = mobileTalkMapper.mSaveTalk(mobileTalkVO);
		return mobileTalkVO; 
	}	
	
	//모바일 해피콜완료 4000 => 5000 으로 바꾸기
	public MobileTalkVO mSaveStat(MobileTalkVO mobileTalkVO) { 
		
		//20220517 정연호 추가. 모바일수행시 KPP오더인지 아닌지 SP돌려서 알아본다음 뭐라도 나오면 KPP오더이니깐 API를 태워서 KPP쪽 상태값을 변경시켜준다
		ErpCommonVO erpCommonVO = new ErpCommonVO();
		erpCommonVO.setInstMobileMId(mobileTalkVO.getInstMobileMId());
		erpCommonVO.setSoStatCd("7500");		//7500 알림톡발송
		erpCommonVO.setDelResn("알림톡발송");
		erpCommonVO = erpCommonMapper.erpCommoSendValueSearchForMobile(erpCommonVO);
		log.info(erpCommonVO.toString());
		//20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
		if(erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals(""))
		{
			log.info("알림톡발송 - kpp 오더입니다.");
			//20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다 
			erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
			if(erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y")) {
				mobileTalkVO = mobileTalkMapper.mSaveStat(mobileTalkVO);
			}
			else {
				mobileTalkVO.setRtnYn("Y");
				mobileTalkVO.setRtnMsg("알림톡발송 실패  - KPP API 오더상태 값 변경 실패");
			}
		}
		//20220517정연호 kpp오더가 아니면 그냥 원래대로 배송취소를 수행함
		else
		{
			mobileTalkVO = mobileTalkMapper.mSaveStat(mobileTalkVO);
		}		
		return mobileTalkVO; 
	}
} 