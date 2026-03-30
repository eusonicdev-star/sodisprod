package com.sonictms.alsys.erp.erp100801.service;

import com.sonictms.alsys.erp.erp100801.entity.Erp100801VO;
import com.sonictms.alsys.erp.erp100801.mapper.Erp100801Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp100801Service {

    private final Erp100801Mapper erp100801Mapper;

    //kpp에서 불러온 데이터를 얼라이언스 테이블에 저장하는 곳
    public Erp100801VO outCmpySave(Erp100801VO erp100801VO) {

        int nCnt = 0;
        for (int i = 0; i < erp100801VO.getResults().size(); i++) {
            erp100801VO.getResults().get(i).setDlvDtFrom(erp100801VO.getDlvDtFrom());
            erp100801VO.getResults().get(i).setDlvDtTo(erp100801VO.getDlvDtTo());
            erp100801VO.getResults().get(i).setSaveUser(erp100801VO.getSaveUser());
            Erp100801VO returnVo = erp100801Mapper.outCmpySave(erp100801VO.getResults().get(i));

            if (!returnVo.getRtnYn().equals("Y")) {
                nCnt++;
            }
        }
        //불러온 데이터를 저장중 오류발생했을때
        if (nCnt != 0) {
            erp100801VO.setRtnYn("N");
            erp100801VO.setRtnMsg(erp100801VO.getResults().size() + "건 중 " + nCnt + "건 오류. 전체 저장실패.");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } else {
            erp100801OutCmpyList(erp100801VO);
            erp100801VO.setRtnYn("Y");
            erp100801VO.setRtnMsg(erp100801VO.getResults().size() + "건 저장.");
        }

        return erp100801VO;
    }

    //kpp에서 불러온 데이터를 얼라이언스 테이블에 저장한것을 불러오는
    public Erp100801VO erp100801OutCmpyList(Erp100801VO erp100801VO) {
        erp100801VO.setResults(erp100801Mapper.erp100801OutCmpyList(erp100801VO));
        return erp100801VO;
    }

    //데이터 체크
    public Erp100801VO erp100801DataChk(Erp100801VO erp100801VO) {
        erp100801Mapper.erp100801DataChk(erp100801VO);
        erp100801VO.setResults(erp100801Mapper.erp100801OutCmpyList(erp100801VO));
        return erp100801VO;
    }

    //테이블에 저장된 외부업체(KPP)의 정보를 얼라이언스의 오더로 입력하기 - 화면의 그리드 한줄한줄을 넘겨받아 한번씩 한번씩 입력한다
    public Erp100801VO erp100801AlOrdrSave(Erp100801VO erp100801VO) {
        for (int i = 0; i < erp100801VO.getResults().size(); i++) {
            erp100801VO.getResults().get(i).setExcelRow(erp100801VO.getResults().get(i).getNo());
            erp100801VO.getResults().get(i).setOutCmpyCd(erp100801VO.getResults().get(i).getDlvCompNm());
            erp100801VO.getResults().get(i).setRefSoNo(erp100801VO.getResults().get(i).getOrgOrdId());
            erp100801VO.getResults().get(i).setOrgnSoNo(erp100801VO.getResults().get(i).getDlvOrdId());
            erp100801VO.getResults().get(i).setSoType(erp100801VO.getResults().get(i).getCnvtTrnDlvOrdTypeCd());
            erp100801VO.getResults().get(i).setSoStatCd("1200");    //1200 고정값
            erp100801VO.getResults().get(i).setAgntCd(erp100801VO.getResults().get(i).getCnvtTrustCustCd());
            erp100801VO.getResults().get(i).setMallCd(erp100801VO.getResults().get(i).getSalesCompanyNm());
            erp100801VO.getResults().get(i).setBrand(erp100801VO.getResults().get(i).getBuyCustNm());
            erp100801VO.getResults().get(i).setOrdrInptDt("");
            erp100801VO.getResults().get(i).setRcptDt(erp100801VO.getResults().get(i).getBuyedDt());
            erp100801VO.getResults().get(i).setRqstDt(erp100801VO.getResults().get(i).getDlvInReqDt());
            erp100801VO.getResults().get(i).setDlvyCnfmDt(erp100801VO.getResults().get(i).getDlvInReqDt());
            erp100801VO.getResults().get(i).setAcptEr(erp100801VO.getResults().get(i).getDlvCustomerNm());
            erp100801VO.getResults().get(i).setOrdrEr(erp100801VO.getResults().get(i).getBuyCustNm());
            erp100801VO.getResults().get(i).setAcptTel1(erp100801VO.getResults().get(i).getDlvPhone1());
            erp100801VO.getResults().get(i).setAcptTel2(erp100801VO.getResults().get(i).getDlvPhone2());
            erp100801VO.getResults().get(i).setPostCd(erp100801VO.getResults().get(i).getCnvtDlvZip());
            erp100801VO.getResults().get(i).setAddr1(erp100801VO.getResults().get(i).getCnvtDlvAddr());
            erp100801VO.getResults().get(i).setAddr2("");
            erp100801VO.getResults().get(i).setDlvyRqstMsg(erp100801VO.getResults().get(i).getEtc1());
            erp100801VO.getResults().get(i).setCustSpclTxt(erp100801VO.getResults().get(i).getEtc2());
            erp100801VO.getResults().get(i).setCostType("");
            erp100801VO.getResults().get(i).setRcptCost("");
            erp100801VO.getResults().get(i).setPassCost("");
            erp100801VO.getResults().get(i).setProdCd(erp100801VO.getResults().get(i).getCnvtDlvProductCd());
            erp100801VO.getResults().get(i).setProdNm(erp100801VO.getResults().get(i).getCnvtDlvProductNm());
            erp100801VO.getResults().get(i).setQty(erp100801VO.getResults().get(i).getDlvQty());
            erp100801VO.getResults().get(i).setDcCd("");
            erp100801VO.getResults().get(i).setUseYn("Y");
            erp100801VO.getResults().get(i).setMemo(erp100801VO.getResults().get(i).getMemo());    //20220625 정연호 수정. 메모 역할 변경으로 기존 공백입력에서 인수증메모입력으로변경
            erp100801VO.getResults().get(i).setInptSys("KPP_IF");
            erp100801VO.getResults().get(i).setDlvReqDt(erp100801VO.getResults().get(i).getDlvReqDt());    //20220625 정연호 추가. 배송예정일 추가
            erp100801Mapper.erp100801AlOrdrSave(erp100801VO.getResults().get(i));
        }
        erp100801VO.setTransChkYn("N");
        erp100801VO.setResults(erp100801Mapper.erp100801OutCmpyList(erp100801VO));
        return erp100801VO;
    }
}