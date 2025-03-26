package com.allianceLogistics.alsys.mobile.mDeliveryCancel.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.allianceLogistics.alsys.mobile.mDeliveryCancel.entity.MobileDeliveryCancelVO;

@Component
@Mapper
public interface MobileDeliveryCancelMapper {

	MobileDeliveryCancelVO mDeliveryCancelSave(MobileDeliveryCancelVO mobileDeliveryCancelVO);
	
}