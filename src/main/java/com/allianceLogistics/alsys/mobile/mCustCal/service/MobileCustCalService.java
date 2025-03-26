package com.allianceLogistics.alsys.mobile.mCustCal.service;


import com.allianceLogistics.alsys.mobile.mCustCal.entity.MobileCustCalVO;
import com.allianceLogistics.alsys.mobile.mCustCal.mapper.MobileCustCalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MobileCustCalService {

    private final MobileCustCalMapper mobileCustCalMapper;

    //고객이 배송 가능/불가능 날짜 불러오기
    public List<MobileCustCalVO> mCustCalDisableDay(MobileCustCalVO mobileCustCalVO) {
        List<MobileCustCalVO> list = mobileCustCalMapper.mCustCalDisableDay(mobileCustCalVO);
        return list;
    }

    //배송날짜 저장하기 (실제는 CAPA_ID 저장하기)
    public MobileCustCalVO mCustCalSaveDate(MobileCustCalVO mobileCustCalVO) {
        mobileCustCalVO = mobileCustCalMapper.mCustCalSaveDate(mobileCustCalVO);
        return mobileCustCalVO;
    }

    //20220224 정연호. 기사평가 신규
    //20220224 정연호 신규. 평가를 했나 안했나 조회하는것
    public MobileCustCalVO mFeedSrch(MobileCustCalVO mobileCustCalVO) {
        mobileCustCalVO = mobileCustCalMapper.mFeedSrch(mobileCustCalVO);
        return mobileCustCalVO;
    }

    //20220224 정연호. 기사평가 신규
    //20220224 정연호 신규. 평가 결과 저장하기
    public MobileCustCalVO mSaveFeedBack(MobileCustCalVO mobileCustCalVO) {
        mobileCustCalVO = mobileCustCalMapper.mSaveFeedBack(mobileCustCalVO);
        return mobileCustCalVO;
    }
} 