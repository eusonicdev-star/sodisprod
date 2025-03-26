package com.allianceLogistics.alsys.erp.erp102001.service;

import com.allianceLogistics.alsys.common.entity.ErpCommonVO;
import com.allianceLogistics.alsys.common.mapper.ErpCommonMapper;
import com.allianceLogistics.alsys.common.service.ErpCommonService;
import com.allianceLogistics.alsys.erp.erp101001.mapper.Erp101001Mapper;
import com.allianceLogistics.alsys.erp.erp102001.entity.Erp102001VO;
import com.allianceLogistics.alsys.erp.erp102001.mapper.Erp102001Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp102001Service {

    private final Erp102001Mapper erp102001Mapper;
    private final Erp101001Mapper erp101001Mapper;
    private final ErpCommonMapper erpCommonMapper;
    private final ErpCommonService erpCommonService;

    //그리드 불러오기
    public List<Erp102001VO> erp102001p1List(Erp102001VO erp102001VO) {
        List<Erp102001VO> list = erp102001Mapper.erp102001p1List(erp102001VO);
        return list;
    }

    //메인화면 주문헤더조회
    public Erp102001VO erp102001SoHeadSrch(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Mapper.erp102001SoHeadSrch(erp102001VO);
        return erp102001VO;
    }

    //메인화면 주문상세조회
    public List<Erp102001VO> erp102001SoDetailSrch(Erp102001VO erp102001VO) {
        List<Erp102001VO> list = erp102001Mapper.erp102001SoDetailSrch(erp102001VO);
        return list;
    }

    //메인화면 상담내역조회
    public Erp102001VO erp102001HappyCallSrch(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Mapper.erp102001HappyCallSrch(erp102001VO);
        return erp102001VO;
    }

    //메인화면 상담정보 등록하기
    public Erp102001VO erp102001CnslSave(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Mapper.erp102001CnslSave(erp102001VO);
        return erp102001VO;
    }

    //해피콜상담리스트 팝업 조회
    public List<Erp102001VO> erp102001p2List(Erp102001VO erp102001VO) {
        List<Erp102001VO> list = erp102001Mapper.erp102001p2List(erp102001VO);
        return list;
    }

    //메인화면 오더정보 변경한것 수정하기
    public Erp102001VO erp102001p1Updt(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Mapper.erp102001p1Updt(erp102001VO);
        return erp102001VO;
    }

    //메인화면 알림톡 조회
    public Erp102001VO erp102001talkCallSrch(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Mapper.erp102001talkCallSrch(erp102001VO);
        return erp102001VO;
    }

    //알림톡 발송할 내용을 불러온다
    public Erp102001VO erp102001p4LoadAlrmTmp(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Mapper.erp102001p4LoadAlrmTmp(erp102001VO);
        return erp102001VO;
    }

    //알림톡발송리스트 팝업 조회
    public List<Erp102001VO> erp102001p3List(Erp102001VO erp102001VO) {
        List<Erp102001VO> list = erp102001Mapper.erp102001p3List(erp102001VO);
        return list;
    }

    // 주문이력보기 팝업의 리스트조회
    public List<Erp102001VO> erp102001p5List(Erp102001VO erp102001VO) {
        List<Erp102001VO> list = erp102001Mapper.erp102001p5List(erp102001VO);
        return list;
    }

    //배송정보 & 이미지
    public Erp102001VO erp102001soDlvyInfoData(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Mapper.erp102001soDlvyInfoData(erp102001VO);
        return erp102001VO;
    }

    /// 메인화면 상담정보 등록하기
    public Erp102001VO erp101001DlvyCnfm(Erp102001VO erp102001VO) {
        //20220517 정연호 kpp오더 라면 조회해서 api로 보낼 값 추출하기
        if (erp102001VO.getOutCmpyCd().equals("KPP")) {
            log.info("배송일확정 - kpp 오더입니다.");
            ErpCommonVO erpCommonVO = new ErpCommonVO();
            erpCommonVO.setSoNoList(erp102001VO.getSoNo());
            erpCommonVO.setSoStatCd("2000");        //2000 배송일 확정 상태
            erpCommonVO.setDelResn(erp102001VO.getDelResn());
            erpCommonVO = erpCommonMapper.erpCommoSendValueSearch(erpCommonVO);
            log.info(erpCommonVO.toString());
            //20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
            if (erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals("")) {
                //20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다
                erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
                if (erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y")) {
                    erp102001VO = erp102001Mapper.erp101001DlvyCnfm(erp102001VO);
                } else {
                    erp102001VO.setRtnYn("N");
                    erp102001VO.setRtnMsg("배송일확정 실패 - KPP API 오더상태 값 변경 실패");
                }
            }
        } else {
            erp102001VO = erp102001Mapper.erp101001DlvyCnfm(erp102001VO);
        }


        return erp102001VO;
    }

    //20211231 정연호 추가. 제품정보 그리드 영역 수정한것을 저장하기 (전체 말고 그리드 영역만)
    public Erp102001VO erp102001UpdtGrid(Erp102001VO erp102001VO) {
        Erp102001VO returnVo = erp102001VO;
        //20211231 정연호 메인화면의 착불비 합계 update
        Erp102001VO vo = erp102001Mapper.erp102001UpdtRcptCost(erp102001VO);

        if (!vo.getRtnYn().equals("Y")) {
            // 헤더에 리턴을 실패라고 보냄
            returnVo.setRtnYn("N");
            returnVo.setRtnMsg("품목수정 실패");

            // 강제롤백	//에러 발생시 강제롤백
            log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return returnVo;
        }

        for (int i = 0; i < erp102001VO.getDetailData().size(); i++) {

            String saveUser = erp102001VO.getSaveUser();
            String tblSoMId = returnVo.getTblSoMId();
            String soNo = returnVo.getSoNo();
            String prodSeq = erp102001VO.getDetailData().get(i).getProdSeq();

            String rqstDt = erp102001VO.getRqstDt();
            String dlvyCnfmDt = erp102001VO.getDlvyCnfmDt();
            String dcCd = erp102001VO.getDcCd();
            String agntCd = erp102001VO.getAgntCd(); // 20211103 정연호 추가. 화주사 코드
            erp102001VO.getDetailData().get(i).setAgntCd(agntCd);// 20211103 정연호 추가. 화주사 코드
            erp102001VO.getDetailData().get(i).setSaveUser(saveUser);
            erp102001VO.getDetailData().get(i).setSaveUser(saveUser);
            erp102001VO.getDetailData().get(i).setTblSoMId(tblSoMId);
            erp102001VO.getDetailData().get(i).setSoNo(soNo);

            erp102001VO.getDetailData().get(i).setRqstDt(rqstDt);
            erp102001VO.getDetailData().get(i).setDlvyCnfmDt(dlvyCnfmDt);
            erp102001VO.getDetailData().get(i).setDcCd(dcCd);
            erp102001VO.getDetailData().get(i).setUseYn("Y");

            //20211231 정연호 추가
            if (erp102001VO.getDetailData().get(i).getRcptCost() != null && !erp102001VO.getDetailData().get(i).getRcptCost().equals("0") && !erp102001VO.getDetailData().get(i).getRcptCost().equals("")) {
                erp102001VO.getDetailData().get(i).setDlvyCostType(erp102001VO.getDlvyCostType());
            }

            // 수정화면에서 새로 추가함
            if (erp102001VO.getDetailData().get(i).getCud().equals("C")) {
                erp102001VO.getDetailData().set(i, erp101001Mapper.erp101001p1DetailSave(erp102001VO.getDetailData().get(i)));
            }
            //20211226 정연호 수정화면에서
            else if (erp102001VO.getDetailData().get(i).getCud().equals("U")) {
                erp102001VO.getDetailData().set(i, erp101001Mapper.erp101001p3UpdtDetail(erp102001VO.getDetailData().get(i)));
            }
            // 수정화면에서 삭제
            else if (erp102001VO.getDetailData().get(i).getCud().equals("D")) {
                erp102001VO.getDetailData().get(i).setUseYn("N");
                erp102001VO.getDetailData().set(i, erp101001Mapper.erp101001p3DelDetail(erp102001VO.getDetailData().get(i)));
            }

            erp102001VO.getDetailData().get(i).setTblSoMId(tblSoMId);
            erp102001VO.getDetailData().get(i).setSoNo(soNo);
            erp102001VO.getDetailData().get(i).setSaveUser(saveUser);
            erp102001VO.getDetailData().get(i).setProdSeq(prodSeq);
        }

        // 반복 돌려서 결과중에 Y 가 아닌게 1개라도 있으면 트랜잭션 강제 롤백
        int errCnt = 0;
        for (int i = 0; i < erp102001VO.getDetailData().size(); i++) {
            if (!erp102001VO.getDetailData().get(i).getRtnYn().equals("Y")) {
                errCnt++;
            }
        }
        if (errCnt > 0) // 에러가 있으면
        {
            // 헤더에 리턴을 실패라고 보냄
            returnVo.setRtnYn("N");
            returnVo.setRtnMsg("품목수정 실패");

            // 강제롤백 //1개라도 에러가 발생하면 모두 롤백
            log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } else { // 에러없이 정상이면
            returnVo.setRtnYn("Y");
            returnVo.setRtnMsg("품목수정 성공");
        }
        return returnVo;
    }
}