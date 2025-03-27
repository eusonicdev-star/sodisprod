package com.sonictms.alsys.mobile.mBarCode.service;

import com.sonictms.alsys.mobile.mBarCode.entity.MobileBarCodeVO;
import com.sonictms.alsys.mobile.mBarCode.mapper.MobileBarCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MobileBarCodeService {

    private final MobileBarCodeMapper mobileBarCodeMapper;

    //모바일 바코드(판매오더번호) 로 상차조회하기
    public MobileBarCodeVO mBarCodeScan(MobileBarCodeVO mobileBarCodeVO) {
        mobileBarCodeVO = mobileBarCodeMapper.mBarCodeScan(mobileBarCodeVO);
        return mobileBarCodeVO;
    }


    //모바일 바코드(판매오더번호) 로 상차변경하기
    public MobileBarCodeVO mBarCodeLiftChange(MobileBarCodeVO mobileBarCodeVO) {
        mobileBarCodeVO = mobileBarCodeMapper.mBarCodeLiftChange(mobileBarCodeVO);
        return mobileBarCodeVO;
    }

} 