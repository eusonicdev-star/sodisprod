package com.sonictms.alsys.mobile.mNoCmpl.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sonictms.alsys.common.entity.ErpCommonVO;
import com.sonictms.alsys.common.mapper.ErpCommonMapper;
import com.sonictms.alsys.common.service.ErpCommonService;
import com.sonictms.alsys.mobile.mNoCmpl.entity.MobileNoCmplVO;
import com.sonictms.alsys.mobile.mNoCmpl.mapper.MobileNoCmplMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class MobileNoCmplService  {

	@Autowired
	private  MobileNoCmplMapper mobileNoCmplMapper;

	//20220517 정연호 kpp오더 상태값 변경하기위해서 추가함
	@Autowired
	private ErpCommonMapper erpCommonMapper;
	//20220517 정연호 kpp오더 상태값 변경하기위해서 추가함
	@Autowired
	private ErpCommonService erpCommonService;
	
	//모바일 미마감 onCreate 조회
	public MobileNoCmplVO mNoCmplOnCreate(MobileNoCmplVO mobileNoCmplVO) { 
		mobileNoCmplVO = mobileNoCmplMapper.mNoCmplOnCreate(mobileNoCmplVO);
		return mobileNoCmplVO; 
	}

	//모바일 미마감 처리 결과 저장 SAVE
	public MobileNoCmplVO mNoCmplSaveStat(MobileNoCmplVO mobileNoCmplVO) { 
		
		//20220517 정연호 추가. 모바일수행시 KPP오더인지 아닌지 SP돌려서 알아본다음 뭐라도 나오면 KPP오더이니깐 API를 태워서 KPP쪽 상태값을 변경시켜준다
		ErpCommonVO erpCommonVO = new ErpCommonVO();
		erpCommonVO.setInstMobileMId(mobileNoCmplVO.getInstMobileMId());
		erpCommonVO.setSoStatCd("8000");		//8000 배송완료(미마감)
		erpCommonVO.setDelResn("배송완료(미마감)");
		erpCommonVO = erpCommonMapper.erpCommoSendValueSearchForMobile(erpCommonVO);
		log.info(erpCommonVO.toString());
		//20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
		if(erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals(""))
		{
			log.info("배송완료(미마감) - kpp 오더입니다.");
			//20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다 
			erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
			if(erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y")) 
			{
				mobileNoCmplVO = mobileNoCmplMapper.mNoCmplSaveStat(mobileNoCmplVO);
			}
			else {
				mobileNoCmplVO.setRtnYn("N");
				mobileNoCmplVO.setRtnMsg("배송완료(미마감)실패 - KPP API 오더상태 값 변경 실패");
			}
		}
		//20220517정연호 kpp오더가 아니면 그냥 원래대로 배송완료(미마감) 수행함
		else
		{
			mobileNoCmplVO = mobileNoCmplMapper.mNoCmplSaveStat(mobileNoCmplVO);
		}		
		return mobileNoCmplVO; 
	}
	
	//모바일 미마감 취소 처리 저장 Del
	public MobileNoCmplVO mNoCmplDel(MobileNoCmplVO mobileNoCmplVO) { 
		
		//20220517 정연호 추가. 모바일수행시 KPP오더인지 아닌지 SP돌려서 알아본다음 뭐라도 나오면 KPP오더이니깐 API를 태워서 KPP쪽 상태값을 변경시켜준다
		ErpCommonVO erpCommonVO = new ErpCommonVO();
		erpCommonVO.setInstMobileMId(mobileNoCmplVO.getInstMobileMId());
		erpCommonVO.setSoStatCd("7000");		//7000 미마감 취소
		erpCommonVO.setDelResn("미마감 취소");
		erpCommonVO = erpCommonMapper.erpCommoSendValueSearchForMobile(erpCommonVO);
		log.info(erpCommonVO.toString());
		//20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
		if(erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals(""))
		{
			log.info("미마감 취소 - kpp 오더입니다.");
			//20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다 
			erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
			if(erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y")) 
			{
				mobileNoCmplVO = mobileNoCmplMapper.mNoCmplDel(mobileNoCmplVO);
			}
			else {
				mobileNoCmplVO.setRtnYn("N");
				mobileNoCmplVO.setRtnMsg("미마감 취소 실패 - KPP API 오더상태 값 변경 실패");
			}
		}
		//20220517정연호 kpp오더가 아니면 그냥 원래대로 배송완료(미마감) 수행함
		else
		{
			mobileNoCmplVO = mobileNoCmplMapper.mNoCmplDel(mobileNoCmplVO);
		}		
		return mobileNoCmplVO; 
	}

	//모바일 미마감 알림톡 결과 저장 SAVE
	public MobileNoCmplVO mNoCmplSaveTalk(MobileNoCmplVO mobileNoCmplVO) { 
		mobileNoCmplVO = mobileNoCmplMapper.mNoCmplSaveTalk(mobileNoCmplVO);
		return mobileNoCmplVO; 
	}
}