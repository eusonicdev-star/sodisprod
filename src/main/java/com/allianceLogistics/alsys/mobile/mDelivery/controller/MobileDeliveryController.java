package com.allianceLogistics.alsys.mobile.mDelivery.controller;

import com.allianceLogistics.alsys.mobile.mDelivery.entity.MobileDeliveryVO;
import com.allianceLogistics.alsys.mobile.mDelivery.service.MobileDeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class MobileDeliveryController {

    private final MobileDeliveryService mobileDeliveryService;

    // 모바일 배송 리스트 조회하기
    @RequestMapping(value = {"mobile/mDeliveryList"}, method = RequestMethod.POST)
    @ResponseBody
    public List<MobileDeliveryVO> mDeliveryList(@RequestBody MobileDeliveryVO mobileDeliveryVO) {

        List<MobileDeliveryVO> list = mobileDeliveryService.mDeliveryList(mobileDeliveryVO);
        return list;
    }

    // 모바일 배송 리스트 순번변경하기
    @RequestMapping(value = {"mobile/mDeliverySeqUpdate"}, method = RequestMethod.POST)
    @ResponseBody
    public MobileDeliveryVO mDeliverySeqUpdate(@RequestBody MobileDeliveryVO mobileDeliveryVO) {
        mobileDeliveryVO = mobileDeliveryService.mDeliverySeqUpdate(mobileDeliveryVO);
        return mobileDeliveryVO;
    }

    // 모바일 배송 리스트 통화 카운트 증가
    @RequestMapping(value = {"mobile/mDeliveryTelUpdate"}, method = RequestMethod.POST)
    @ResponseBody
    public MobileDeliveryVO mDeliveryTelUpdate(@RequestBody MobileDeliveryVO mobileDeliveryVO) {
        mobileDeliveryVO = mobileDeliveryService.mDeliveryTelUpdate(mobileDeliveryVO);
        return mobileDeliveryVO;
    }

    // 모바일 배송 상세 조회하기
    @RequestMapping(value = {"mobile/mDeliveryDetailSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public List<MobileDeliveryVO> mDeliveryDetailSrch(@RequestBody MobileDeliveryVO mobileDeliveryVO) {
        List<MobileDeliveryVO> list = mobileDeliveryService.mDeliveryDetailSrch(mobileDeliveryVO);
        return list;
    }

    // 모바일 배송 상차 완료 하기
    @RequestMapping(value = {"mobile/mDeliveryLiftSave"}, method = RequestMethod.POST)
    @ResponseBody

    public MobileDeliveryVO mDeliveryLiftSave(@RequestBody List<MobileDeliveryVO> mobileDeliveryVO) {
        MobileDeliveryVO rtnVO;
        rtnVO = mobileDeliveryService.mDeliveryLiftSave(mobileDeliveryVO);

        //20220517 정연호 kpp 오더상태값 변경 때문에 값 저장안할려고 - 테스트만하려고 - 강제롤백 추가함
        //log.info("20220517 정연호 테스트용 저장안하려고 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        return rtnVO;
    }

    // 모바일 배송 상차 취소 하기
    @RequestMapping(value = {"mobile/mDeliveryLiftCancel"}, method = RequestMethod.POST)
    @ResponseBody

    public MobileDeliveryVO mDeliveryLiftCancel(@RequestBody MobileDeliveryVO mobileDeliveryVO) {

        mobileDeliveryVO = mobileDeliveryService.mDeliveryLiftCancel(mobileDeliveryVO);

        return mobileDeliveryVO;
    }

}