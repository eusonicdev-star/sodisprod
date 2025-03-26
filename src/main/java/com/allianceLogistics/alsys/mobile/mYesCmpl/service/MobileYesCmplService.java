package com.allianceLogistics.alsys.mobile.mYesCmpl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allianceLogistics.alsys.common.entity.ErpCommonVO;
import com.allianceLogistics.alsys.common.mapper.ErpCommonMapper;
import com.allianceLogistics.alsys.common.service.ErpCommonService;
import com.allianceLogistics.alsys.mobile.mYesCmpl.entity.MobileYesCmplVO;
import com.allianceLogistics.alsys.mobile.mYesCmpl.mapper.MobileYesCmplMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class MobileYesCmplService  {
	 
	@Autowired
	private  MobileYesCmplMapper mobileYesCmplMapper;
	
	//20220517 정연호 kpp오더 상태값 변경하기위해서 추가함
	@Autowired
	private ErpCommonMapper erpCommonMapper;
	//20220517 정연호 kpp오더 상태값 변경하기위해서 추가함
	@Autowired
	private ErpCommonService erpCommonService;
	
	//모바일 배송완료 onCreate 조회
	public MobileYesCmplVO mYesCmplOnCreate(MobileYesCmplVO mobileYesCmplVO) { 
		mobileYesCmplVO = mobileYesCmplMapper.mYesCmplOnCreate(mobileYesCmplVO);
		return mobileYesCmplVO; 
	}

	//모바일 배송완료 처리 저장 SAVE
	public MobileYesCmplVO mYesCmplSaveStat(MobileYesCmplVO mobileYesCmplVO) { 
		
		//20220517 정연호 추가. 모바일수행시 KPP오더인지 아닌지 SP돌려서 알아본다음 뭐라도 나오면 KPP오더이니깐 API를 태워서 KPP쪽 상태값을 변경시켜준다
		ErpCommonVO erpCommonVO = new ErpCommonVO();
		erpCommonVO.setInstMobileMId(mobileYesCmplVO.getInstMobileMId());
		erpCommonVO.setSoStatCd("9999");		//9999 배송완료
		erpCommonVO.setDelResn("배송완료");
		erpCommonVO = erpCommonMapper.erpCommoSendValueSearchForMobile(erpCommonVO);
		log.info(erpCommonVO.toString());
		//20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
		if(erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals(""))
		{
			log.info("배송완료 - kpp 오더입니다.");
			//20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다 
			erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
			if(erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y")) 
			{
				mobileYesCmplVO = mobileYesCmplMapper.mYesCmplSaveStat(mobileYesCmplVO);
			}
			else {
				mobileYesCmplVO.setRtnYn("N");
				mobileYesCmplVO.setRtnMsg("배송완료 실패 - KPP API 오더상태 값 변경 실패");
			}
		}
		//20220517정연호 kpp오더가 아니면 그냥 원래대로 배송완료 수행함
		else
		{
			mobileYesCmplVO = mobileYesCmplMapper.mYesCmplSaveStat(mobileYesCmplVO);
		}		
		return mobileYesCmplVO; 
	}
	
	//모바일 배송완료 취소 처리 저장 Del
	public MobileYesCmplVO mYesCmplDel(MobileYesCmplVO mobileYesCmplVO) { 
		
		//20220517 정연호 추가. 모바일수행시 KPP오더인지 아닌지 SP돌려서 알아본다음 뭐라도 나오면 KPP오더이니깐 API를 태워서 KPP쪽 상태값을 변경시켜준다
		ErpCommonVO erpCommonVO = new ErpCommonVO();
		erpCommonVO.setInstMobileMId(mobileYesCmplVO.getInstMobileMId());
		erpCommonVO.setSoStatCd("7000");		//7000 배송완료 취소
		erpCommonVO.setDelResn("배송완료 취소");
		erpCommonVO = erpCommonMapper.erpCommoSendValueSearchForMobile(erpCommonVO);
		log.info(erpCommonVO.toString());
		//20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
		if(erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals(""))
		{
			log.info("배송완료 취소 - kpp 오더입니다.");
			//20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다 
			erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
			if(erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y")) 
			{
				mobileYesCmplVO = mobileYesCmplMapper.mYesCmplDel(mobileYesCmplVO);
			}
			else {
				mobileYesCmplVO.setRtnYn("N");
				mobileYesCmplVO.setRtnMsg("배송완료 취소 실패 - KPP API 오더상태 값 변경 실패");
			}
		}
		//20220517정연호 kpp오더가 아니면 그냥 원래대로 배송완료 수행함
		else
		{
			mobileYesCmplVO = mobileYesCmplMapper.mYesCmplDel(mobileYesCmplVO);
		}		
		return mobileYesCmplVO; 
	}

	//모바일 배송완료 알림톡 결과 저장
	public MobileYesCmplVO mYesCmplSaveTalk(MobileYesCmplVO mobileYesCmplVO) { 
		mobileYesCmplVO = mobileYesCmplMapper.mYesCmplSaveTalk(mobileYesCmplVO);
		return mobileYesCmplVO; 
	}
}