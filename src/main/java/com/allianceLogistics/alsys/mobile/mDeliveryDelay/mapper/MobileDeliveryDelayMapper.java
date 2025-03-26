package com.allianceLogistics.alsys.mobile.mDeliveryDelay.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.allianceLogistics.alsys.mobile.mDeliveryDelay.entity.MobileDeliveryDelayVO;

@Component
@Mapper
public interface MobileDeliveryDelayMapper {

	List<MobileDeliveryDelayVO> mDeliveryDelayList(MobileDeliveryDelayVO mobileDeliveryDelayVO);
	
	
	MobileDeliveryDelayVO mDeliveryDelaySave(MobileDeliveryDelayVO mobileDeliveryDelayVO);
	
	
}