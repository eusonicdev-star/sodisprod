package com.sonictms.alsys.mobile.mDeliveryCancel.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sonictms.alsys.common.entity.ErpCommonVO;
import com.sonictms.alsys.common.mapper.ErpCommonMapper;
import com.sonictms.alsys.common.service.ErpCommonService;
import com.sonictms.alsys.mobile.mDeliveryCancel.entity.MobileDeliveryCancelVO;
import com.sonictms.alsys.mobile.mDeliveryCancel.mapper.MobileDeliveryCancelMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class MobileDeliveryCancelService  {

	@Autowired
	private  MobileDeliveryCancelMapper mobileDeliveryCancelMapper;

	//20220517 정연호 kpp오더 상태값 변경하기위해서 추가함
	@Autowired
	private ErpCommonMapper erpCommonMapper;
	//20220517 정연호 kpp오더 상태값 변경하기위해서 추가함
	@Autowired
	private ErpCommonService erpCommonService;
	
	//배송취소 버튼 누를때 수행
	public MobileDeliveryCancelVO mDeliveryCancelSave(MobileDeliveryCancelVO mobileDeliveryCancelVO) { 
		
		//20220517 정연호 추가. 모바일수행시 KPP오더인지 아닌지 SP돌려서 알아본다음 뭐라도 나오면 KPP오더이니깐 API를 태워서 KPP쪽 상태값을 변경시켜준다
		ErpCommonVO erpCommonVO = new ErpCommonVO();
		erpCommonVO.setInstMobileMId(mobileDeliveryCancelVO.getInstMobileMId());
		erpCommonVO.setSoStatCd("8060");		//8060 배송취소
		erpCommonVO.setDelResn("배송취소");
		erpCommonVO = erpCommonMapper.erpCommoSendValueSearchForMobile(erpCommonVO);
		log.info(erpCommonVO.toString());
		//20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
		if(erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals(""))
		{
			log.info("배송취소 - kpp 오더입니다.");
			//20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다 
			erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
			if(erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y")) {
				mobileDeliveryCancelVO = mobileDeliveryCancelMapper.mDeliveryCancelSave(mobileDeliveryCancelVO);
			}
			else {
				mobileDeliveryCancelVO.setRtnYn("N");
				mobileDeliveryCancelVO.setRtnMsg("배송취소실패 - KPP API 오더상태 값 변경 실패");
			}
		}
		//20220517정연호 kpp오더가 아니면 그냥 원래대로 배송취소를 수행함
		else
		{
			mobileDeliveryCancelVO = mobileDeliveryCancelMapper.mDeliveryCancelSave(mobileDeliveryCancelVO);
		}		
		return mobileDeliveryCancelVO; 
	}
}