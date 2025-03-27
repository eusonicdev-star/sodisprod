package com.sonictms.alsys.mobile.mDelivery.mapper;

import com.sonictms.alsys.mobile.mDelivery.entity.MobileDeliveryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MobileDeliveryMapper {

    //모바일 배송 리스트 조회하기
    List<MobileDeliveryVO> mDeliveryList(MobileDeliveryVO mobileDeliveryVO);

    //모바일 배송 리스트 순번변경하기
    MobileDeliveryVO mDeliverySeqUpdate(MobileDeliveryVO mobileDeliveryVO);

    //모바일 배송 리스트 통화 카운트 증가
    MobileDeliveryVO mDeliveryTelUpdate(MobileDeliveryVO mobileDeliveryVO);

    //모바일 배송 상세 조회하기
    List<MobileDeliveryVO> mDeliveryDetailSrch(MobileDeliveryVO mobileDeliveryVO);

    //모바일 배송 상차 완료 하기
    MobileDeliveryVO mDeliveryLiftSave(MobileDeliveryVO mobileDeliveryVO);

    //모바일 배송 상차 취소 하기
    MobileDeliveryVO mDeliveryLiftCancel(MobileDeliveryVO mobileDeliveryVO);
}