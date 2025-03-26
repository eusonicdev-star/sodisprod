package com.allianceLogistics.alsys.mobile.mDelivery.service;

           import com.allianceLogistics.alsys.common.entity.ErpCommonVO;
import com.allianceLogistics.alsys.common.mapper.ErpCommonMapper;
import com.allianceLogistics.alsys.common.service.ErpCommonService;
import com.allianceLogistics.alsys.mobile.mDelivery.entity.MobileDeliveryVO;
import com.allianceLogistics.alsys.mobile.mDelivery.mapper.MobileDeliveryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MobileDeliveryService {

    private final MobileDeliveryMapper mobileDeliveryMapper;
    private final ErpCommonMapper erpCommonMapper;
    private final ErpCommonService erpCommonService;

    //모바일 배송 리스트 조회하기
    public List<MobileDeliveryVO> mDeliveryList(MobileDeliveryVO mobileDeliveryVO) {
        List<MobileDeliveryVO> list = mobileDeliveryMapper.mDeliveryList(mobileDeliveryVO);
        return list;
    }

    //모바일 배송 리스트 순번변경하기
    public MobileDeliveryVO mDeliverySeqUpdate(MobileDeliveryVO mobileDeliveryVO) {
        mobileDeliveryVO = mobileDeliveryMapper.mDeliverySeqUpdate(mobileDeliveryVO);
        return mobileDeliveryVO;
    }

    //모바일 배송 리스트 통화 카운트 증가
    public MobileDeliveryVO mDeliveryTelUpdate(MobileDeliveryVO mobileDeliveryVO) {
        mobileDeliveryVO = mobileDeliveryMapper.mDeliveryTelUpdate(mobileDeliveryVO);
        return mobileDeliveryVO;
    }

    //모바일 배송 상세 조회하기
    public List<MobileDeliveryVO> mDeliveryDetailSrch(MobileDeliveryVO mobileDeliveryVO) {
        List<MobileDeliveryVO> list = mobileDeliveryMapper.mDeliveryDetailSrch(mobileDeliveryVO);
        return list;
    }

    //모바일 배송 상차 완료 하기
    public MobileDeliveryVO mDeliveryLiftSave(List<MobileDeliveryVO> mobileDeliveryVO) {
        MobileDeliveryVO rtnVO = new MobileDeliveryVO();

        //20220517 정연호 추가. 모바일수행시 KPP오더인지 아닌지 SP돌려서 알아본다음 뭐라도 나오면 KPP오더이니깐 API를 태워서 KPP쪽 상태값을 변경시켜준다
        ErpCommonVO erpCommonVO = new ErpCommonVO();
        erpCommonVO.setInstMobileMId(mobileDeliveryVO.get(0).getInstMobileMId());
        erpCommonVO.setSoStatCd("7000");        //7000 상차
        erpCommonVO.setDelResn("상차");
        erpCommonVO = erpCommonMapper.erpCommoSendValueSearchForMobile(erpCommonVO);
        log.info(erpCommonVO.toString());
        //20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
        if (erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals("")) {
            log.info("상차 - kpp 오더입니다.");
            //20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다
            erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
            if (!(erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y"))) {
                rtnVO.setRtnYn("N");
                rtnVO.setRtnMsg("상차 실패 - KPP API 오더상태 값 변경 실패");
                //강제롤백
                log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly() 상차 실패 - KPP API 오더상태 값 변경 실패");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return rtnVO;
            }
        }


        for (int i = 0; i < mobileDeliveryVO.size(); i++) {
            mobileDeliveryVO.set(i, mobileDeliveryMapper.mDeliveryLiftSave(mobileDeliveryVO.get(i)));
        }

        int errCnt = 0;
        for (int j = 0; j < mobileDeliveryVO.size(); j++) {
            if (mobileDeliveryVO.get(j) == null || !mobileDeliveryVO.get(j).getRtnYn().equals("Y")) {
                errCnt++;
            }
        }
        if (errCnt > 0) {
            rtnVO.setRtnYn("N");
            rtnVO.setRtnMsg("상차 실패");
            //강제롤백
            log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } else {
            rtnVO.setRtnYn("Y");
            rtnVO.setRtnMsg("상차 성공");
        }
        return rtnVO;
    }

    //모바일 배송 상차 취소 하기
    public MobileDeliveryVO mDeliveryLiftCancel(MobileDeliveryVO mobileDeliveryVO) {
        mobileDeliveryVO = mobileDeliveryMapper.mDeliveryLiftCancel(mobileDeliveryVO);
        return mobileDeliveryVO;
    }
} 