package com.allianceLogistics.alsys.mobile.mCustCal.mapper;

import com.allianceLogistics.alsys.mobile.mCustCal.entity.MobileCustCalVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MobileCustCalMapper {
    //고객이 배송 가능/불가능 날짜 불러오기
    List<MobileCustCalVO> mCustCalDisableDay(MobileCustCalVO mobileCustCalVO);

    //배송날짜 저장하기 (실제는 CAPA_ID 저장하기)
    MobileCustCalVO mCustCalSaveDate(MobileCustCalVO mobileCustCalVO);

    //20220224 정연호. 기사평가 신규
    //20220224 정연호 신규. 평가를 했나 안했나 조회하는것
    MobileCustCalVO mFeedSrch(MobileCustCalVO mobileCustCalVO);

    //20220224 정연호. 기사평가 신규
    //20220224 정연호 신규. 평가 결과 저장하기
    MobileCustCalVO mSaveFeedBack(MobileCustCalVO mobileCustCalVO);
}