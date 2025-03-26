package com.allianceLogistics.alsys.mobile.mBarCode.controller;

import com.allianceLogistics.alsys.mobile.mBarCode.entity.MobileBarCodeVO;
import com.allianceLogistics.alsys.mobile.mBarCode.service.MobileBarCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Transactional // 트랜잭션 오류시 롤백
@Controller
public class MobileBarCodeController {

    private final MobileBarCodeService mobileBarCodeService;

    // 모바일 바코드(판매오더번호) 로 상차조회하기
    @RequestMapping(value = {"mobile/mBarCodeScan"}, method = RequestMethod.POST)
    @ResponseBody
    public MobileBarCodeVO mBarCodeScan(@RequestBody MobileBarCodeVO mobileBarCodeVO) {
        mobileBarCodeVO = mobileBarCodeService.mBarCodeScan(mobileBarCodeVO);
        return mobileBarCodeVO;
    }

    // 모바일 바코드(판매오더번호) 로 상차변경 하기
    @RequestMapping(value = {"mobile/mBarCodeLiftChange"}, method = RequestMethod.POST)
    @ResponseBody

    public MobileBarCodeVO mBarCodeLiftChange(@RequestBody MobileBarCodeVO mobileBarCodeVO) {
        mobileBarCodeVO = mobileBarCodeService.mBarCodeLiftChange(mobileBarCodeVO);
        return mobileBarCodeVO;
    }

}