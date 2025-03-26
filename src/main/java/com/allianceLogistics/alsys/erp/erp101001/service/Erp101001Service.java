package com.allianceLogistics.alsys.erp.erp101001.service;

import com.allianceLogistics.alsys.common.entity.ErpCommonVO;
import com.allianceLogistics.alsys.common.mapper.ErpCommonMapper;
import com.allianceLogistics.alsys.common.service.ErpCommonService;
import com.allianceLogistics.alsys.erp.erp101001.entity.Erp101001VO;
import com.allianceLogistics.alsys.erp.erp101001.mapper.Erp101001Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp101001Service {

    private final Erp101001Mapper erp101001Mapper;
    private final ErpCommonMapper erpCommonMapper;
    private final ErpCommonService erpCommonService;

    // 그리드 불러오기
    public List<Erp101001VO> erp101001List(Erp101001VO erp101001VO) {
        return erp101001Mapper.erp101001List(erp101001VO);
    }

    // 배송확정일 가능한 날짜 조회
    public List<Erp101001VO> erp101001p1List(Erp101001VO erp101001VO) {
        return erp101001Mapper.erp101001p1List(erp101001VO);
    }

    // 주문 저장하기 (등록하는 화면)
    public Erp101001VO erp101001p1Save(Erp101001VO erp101001VO) {
        // 1. 헤더 저장
        Erp101001VO returnVo = erp101001Mapper.erp101001p1HeaderSave(erp101001VO);
        // 2. 헤더에서 리턴 받은 고유 아이디 와 AL 오더
        if (returnVo.getTblSoMId() != null && returnVo.getTblSoMId() != "" && returnVo.getSoNo() != null && returnVo.getSoNo() != "") {
            // 3. 헤더저장에서 반환받은 TBL_SO_M_ID 와 SO_NO 로 상세 저장
            for (int i = 0; i < erp101001VO.getDetailData().size(); i++) {

                String saveUser = erp101001VO.getSaveUser();
                String tblSoMId = returnVo.getTblSoMId();
                String soNo = returnVo.getSoNo();
                String prodSeq = erp101001VO.getDetailData().get(i).getProdSeq();

                String rqstDt = erp101001VO.getRqstDt();
                String dlvyCnfmDt = erp101001VO.getDlvyCnfmDt();
                String dcCd = erp101001VO.getDcCd();
                String agntCd = erp101001VO.getAgntCd(); // 20211103 정연호 추가. 화주사 코드
                erp101001VO.getDetailData().get(i).setAgntCd(agntCd);// 20211103 정연호 추가. 화주사 코드
                erp101001VO.getDetailData().get(i).setSaveUser(saveUser);
                erp101001VO.getDetailData().get(i).setTblSoMId(tblSoMId);
                erp101001VO.getDetailData().get(i).setSoNo(soNo);

                erp101001VO.getDetailData().get(i).setRqstDt(rqstDt);
                erp101001VO.getDetailData().get(i).setDlvyCnfmDt(dlvyCnfmDt);
                erp101001VO.getDetailData().get(i).setDcCd(dcCd);

                erp101001VO.getDetailData().get(i).setUseYn("Y");

                //20211231 정연호 추가
                if (erp101001VO.getDetailData().get(i).getRcptCost() != null && !erp101001VO.getDetailData().get(i).getRcptCost().equals("0") && !erp101001VO.getDetailData().get(i).getRcptCost().equals("")) {
                    erp101001VO.getDetailData().get(i).setDlvyCostType(erp101001VO.getDlvyCostType());
                }

                erp101001VO.getDetailData().set(i, erp101001Mapper.erp101001p1DetailSave(erp101001VO.getDetailData().get(i)));
                erp101001VO.getDetailData().get(i).setTblSoMId(tblSoMId);
                erp101001VO.getDetailData().get(i).setSoNo(soNo);
                erp101001VO.getDetailData().get(i).setSaveUser(saveUser);
                erp101001VO.getDetailData().get(i).setProdSeq(prodSeq);
            }

            // 반복 돌려서 결과중에 Y 가 아닌게 1개라도 있으면 트랜잭션 강제 롤백
            int errCnt = 0;
            for (int i = 0; i < erp101001VO.getDetailData().size(); i++) {
                if (!erp101001VO.getDetailData().get(i).getRtnYn().equals("Y")) {
                    errCnt++;
                }
            }
            if (errCnt > 0) { // 에러가 있으면
                // 헤더에 리턴을 실패라고 보냄
                returnVo.setRtnYn("N");
                returnVo.setRtnMsg("주문입력 헤더입력 실패");

                // 강제롤백
                log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } else { // 에러없이 정상이면
                returnVo.setDetailData(erp101001VO.getDetailData());
            }
        }
        // 헤더 입력의 결과 (고유ID,AL 오더) 안넘어 왔다면 강제로 롤백
        else {
            returnVo.setRtnYn("N");
            returnVo.setRtnMsg("주문입력 헤더입력 실패");
            // 강제롤백
            log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return returnVo;
    }

    // 반품주문 저장하기
    public Erp101001VO erp101001p5Save(Erp101001VO erp101001VO) {
        // 1. 헤더 저장
        Erp101001VO returnVo = erp101001Mapper.erp101001p5HeaderSave(erp101001VO);
        // 2. 헤더에서 리턴 받은 고유 아이디 와 AL 오더
        log.info("반품주문 저장-헤더- 사용자      - headerVo.getSaveUser() : " + erp101001VO.getSaveUser());
        log.info("반품주문 저장-헤더- 주문 고유 ID - headerVo.getTblSoMId() : " + returnVo.getTblSoMId());
        log.info("반품주문 저장-헤더- 주문 번호    - headerVo.getSoNo()     : " + returnVo.getSoNo());

        if (returnVo != null && returnVo.getTblSoMId() != null && returnVo.getTblSoMId() != "" && returnVo.getSoNo() != null
                && returnVo.getSoNo() != "") {
            // 3. 헤더저장에서 반환받은 TBL_SO_M_ID 와 SO_NO 로 상세 저장
            for (int i = 0; i < erp101001VO.getDetailData().size(); i++) {

                String saveUser = erp101001VO.getSaveUser();
                String tblSoMId = returnVo.getTblSoMId();
                String soNo = returnVo.getSoNo(); // 헤더에서 넘겨받은 오더번호
                String prodSeq = erp101001VO.getDetailData().get(i).getProdSeq();

                String rqstDt = erp101001VO.getRqstDt();
                String dlvyCnfmDt = erp101001VO.getDlvyCnfmDt();
                String dcCd = erp101001VO.getDcCd();

                String agntCd = erp101001VO.getAgntCd(); // 20211103 정연호 추가. 화주사 코드
                erp101001VO.getDetailData().get(i).setAgntCd(agntCd);// 20211103 정연호 추가. 화주사 코드

                erp101001VO.getDetailData().get(i).setSaveUser(saveUser);
                erp101001VO.getDetailData().get(i).setSaveUser(saveUser);
                erp101001VO.getDetailData().get(i).setTblSoMId(tblSoMId);
                erp101001VO.getDetailData().get(i).setSoNo(soNo);

                erp101001VO.getDetailData().get(i).setRqstDt(rqstDt);
                erp101001VO.getDetailData().get(i).setDlvyCnfmDt(dlvyCnfmDt);
                erp101001VO.getDetailData().get(i).setDcCd(dcCd);
                erp101001VO.getDetailData().get(i).setUseYn("Y");

                //20211231 정연호 추가
                if (erp101001VO.getDetailData().get(i).getRcptCost() != null && !erp101001VO.getDetailData().get(i).getRcptCost().equals("0") && !erp101001VO.getDetailData().get(i).getRcptCost().equals("")) {
                    erp101001VO.getDetailData().get(i).setDlvyCostType(erp101001VO.getDlvyCostType());
                }

                erp101001VO.getDetailData().set(i, erp101001Mapper.erp101001p5DetailSave(erp101001VO.getDetailData().get(i)));
                erp101001VO.getDetailData().get(i).setTblSoMId(tblSoMId);
                erp101001VO.getDetailData().get(i).setSoNo(soNo);
                erp101001VO.getDetailData().get(i).setSaveUser(saveUser);
                erp101001VO.getDetailData().get(i).setProdSeq(prodSeq);
            }

            // 반복 돌려서 결과중에 Y 가 아닌게 1개라도 있으면 트랜잭션 강제 롤백
            int errCnt = 0;
            for (int i = 0; i < erp101001VO.getDetailData().size(); i++) {
                if (!erp101001VO.getDetailData().get(i).getRtnYn().equals("Y")) {
                    errCnt++;
                }
            }
            if (errCnt > 0) // 에러가 있으면
            {
                // 헤더에 리턴을 실패라고 보냄
                returnVo.setRtnYn("N");
                returnVo.setRtnMsg("주문입력 헤더입력 실패");

                // 강제롤백
                log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } else {    // 에러없이 정상이면
                returnVo.setDetailData(erp101001VO.getDetailData());
            }
        }
        // 헤더 입력의 결과 (고유ID,AL 오더) 안넘어 왔다면 강제로 롤백
        else {
            returnVo.setRtnYn("N");
            returnVo.setRtnMsg("주문입력 헤더입력 실패");
            // 강제롤백
            log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return returnVo;
    }

    // 주문수정 팝업에서 헤더 불러오기 //헤더는 1줄만 불러옴
    public Erp101001VO erp101001p3HeaderList(Erp101001VO erp101001VO) {
        erp101001VO = erp101001Mapper.erp101001p3HeaderList(erp101001VO);
        return erp101001VO;
    }

    // 주문수정 팝업에서 상세 불러오기 //상세는 리스트
    public List<Erp101001VO> erp101001p3DetailList(Erp101001VO erp101001VO) {
        List<Erp101001VO> list = erp101001Mapper.erp101001p3DetailList(erp101001VO);
        return list;
    }

    // 상세(제품정보) 삭제하기
    public Erp101001VO erp101001p3DelDetail(Erp101001VO erp101001VO) {
        erp101001VO = erp101001Mapper.erp101001p3DelDetail(erp101001VO);
        return erp101001VO;
    }

    // 주문 수정하기
    public Erp101001VO erp101001p3Updt(Erp101001VO erp101001VO) {
        Erp101001VO returnVo = null;
        //20220517 정연호 추가. kpp오더일경우 kpp api를 태워서 kpp쪽이 변경되어지면 그다음에 얼라이언스 상태값을 바꾼다
        //20220517 정연호 kpp오더 라면 조회해서 api로 보낼 값 추출하기
        if (erp101001VO.getOutCmpyCd().equals("KPP")) {
            log.info("주문상태 수정 - kpp 오더입니다.");
            ErpCommonVO erpCommonVO = new ErpCommonVO();
            erpCommonVO.setSoNoList(erp101001VO.getSoNo());
            erpCommonVO.setSoStatCd(erp101001VO.getSoStatCd());
            erpCommonVO.setDelResn(erp101001VO.getDelResn());
            erpCommonVO = erpCommonMapper.erpCommoSendValueSearch(erpCommonVO);
            log.info(erpCommonVO.toString());
            //20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
            if (erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals("")) {
                //20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다
                erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
                if (erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y")) {        //20220517 정연호. kpp쪽 상태변경에 성공하여 Y가 넘어오면 헤더수정을 한다
                    // 1. 헤더 수정
                    returnVo = erp101001Mapper.erp101001p1HeaderUpdt(erp101001VO);
                } else {
                    returnVo = null;
                }
            }
        } else {        //20220517 정연호 추가. kpp가 아니면 헤더 수정을 원래대로 그냥 진행한다
            // 1. 헤더 수정
            returnVo = erp101001Mapper.erp101001p1HeaderUpdt(erp101001VO);
        }


        // 2. 헤더에서 리턴 받은 고유 아이디
        if (returnVo != null) {
            log.info("주문 수정-헤더- 사용자      - headerVo.getSaveUser() : " + erp101001VO.getSaveUser());
            log.info("주문 수정-헤더- 주문 고유 ID - headerVo.getTblSoMId() : " + returnVo.getTblSoMId());
            log.info("주문 수정-헤더- 주문 번호    - headerVo.getSoNo()     : " + returnVo.getSoNo());
        }
        // 헤더수정이 성공했으면 리턴값이 있다
        if (returnVo != null && returnVo.getTblSoMId() != null && returnVo.getTblSoMId() != "" && returnVo.getSoNo() != null
                && returnVo.getSoNo() != "") {
            // 3. 헤더저장에서 반환받은 TBL_SO_M_ID 와 SO_NO 로 상세 저장(상세는 수정없음. 모두 신규저장임)
            for (int i = 0; i < erp101001VO.getDetailData().size(); i++) {

                String saveUser = erp101001VO.getSaveUser();
                String tblSoMId = returnVo.getTblSoMId();
                String soNo = returnVo.getSoNo();
                String prodSeq = erp101001VO.getDetailData().get(i).getProdSeq();

                String rqstDt = erp101001VO.getRqstDt();
                String dlvyCnfmDt = erp101001VO.getDlvyCnfmDt();
                String dcCd = erp101001VO.getDcCd();
                String agntCd = erp101001VO.getAgntCd(); // 20211103 정연호 추가. 화주사 코드
                erp101001VO.getDetailData().get(i).setAgntCd(agntCd);// 20211103 정연호 추가. 화주사 코드
                erp101001VO.getDetailData().get(i).setSaveUser(saveUser);
                erp101001VO.getDetailData().get(i).setSaveUser(saveUser);
                erp101001VO.getDetailData().get(i).setTblSoMId(tblSoMId);
                erp101001VO.getDetailData().get(i).setSoNo(soNo);

                erp101001VO.getDetailData().get(i).setRqstDt(rqstDt);
                erp101001VO.getDetailData().get(i).setDlvyCnfmDt(dlvyCnfmDt);
                erp101001VO.getDetailData().get(i).setDcCd(dcCd);
                erp101001VO.getDetailData().get(i).setUseYn("Y");

                //20211231 정연호 추가
                if (erp101001VO.getDetailData().get(i).getRcptCost() != null && !erp101001VO.getDetailData().get(i).getRcptCost().equals("0") && !erp101001VO.getDetailData().get(i).getRcptCost().equals("")) {
                    erp101001VO.getDetailData().get(i).setDlvyCostType(erp101001VO.getDlvyCostType());
                }

                // 수정화면에서 새로 추가함
                if (erp101001VO.getDetailData().get(i).getCud().equals("C")) {
                    erp101001VO.getDetailData().set(i, erp101001Mapper.erp101001p1DetailSave(erp101001VO.getDetailData().get(i)));
                }
                //20211226 정연호 수정화면에서
                else if (erp101001VO.getDetailData().get(i).getCud().equals("U")) {
                    erp101001VO.getDetailData().set(i, erp101001Mapper.erp101001p3UpdtDetail(erp101001VO.getDetailData().get(i)));
                }
                // 수정화면에서 삭제
                else if (erp101001VO.getDetailData().get(i).getCud().equals("D")) {
                    erp101001VO.getDetailData().get(i).setUseYn("N");
                    erp101001VO.getDetailData().set(i, erp101001Mapper.erp101001p3DelDetail(erp101001VO.getDetailData().get(i)));
                }

                erp101001VO.getDetailData().get(i).setTblSoMId(tblSoMId);
                erp101001VO.getDetailData().get(i).setSoNo(soNo);
                erp101001VO.getDetailData().get(i).setSaveUser(saveUser);
                erp101001VO.getDetailData().get(i).setProdSeq(prodSeq);
            }

            // 반복 돌려서 결과중에 Y 가 아닌게 1개라도 있으면 트랜잭션 강제 롤백
            int errCnt = 0;
            for (int i = 0; i < erp101001VO.getDetailData().size(); i++) {
                if (!erp101001VO.getDetailData().get(i).getRtnYn().equals("Y")) {
                    errCnt++;
                }
            }
            if (errCnt > 0) // 에러가 있으면
            {
                // 헤더에 리턴을 실패라고 보냄
                returnVo.setRtnYn("N");
                returnVo.setRtnMsg("주문수정 품목 입력/수정 실패");

                // 강제롤백
                log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            } else {    // 에러없이 정상이면
                returnVo.setDetailData(erp101001VO.getDetailData());
            }
        }
        // 헤더 입력의 결과 (고유ID,AL 오더) 안넘어 왔다면 강제로 롤백
        else {
            returnVo.setRtnYn("N");
            returnVo.setRtnMsg("주문수정 헤더수정 실패");
            // 강제롤백
            log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return returnVo;
    }

    // 원오더찾기 팝업에서 리스트조회
    public List<Erp101001VO> erp101001p4List(Erp101001VO erp101001VO) {
        List<Erp101001VO> list = erp101001Mapper.erp101001p4List(erp101001VO);
        return list;
    }

    // 반품등록 팝업에서 헤더 불러오기 //헤더는 1줄만 불러옴
    public Erp101001VO erp101001p5HeaderList(Erp101001VO erp101001VO) {
        erp101001VO = erp101001Mapper.erp101001p5HeaderList(erp101001VO);
        return erp101001VO;
    }

    // 반품등록 팝업에서 상세 불러오기 //상세는 리스트
    public List<Erp101001VO> erp101001p5DetailList(Erp101001VO erp101001VO) {
        List<Erp101001VO> list = erp101001Mapper.erp101001p5DetailList(erp101001VO);
        return list;
    }

    /// 우편번호로 물류센터 찾기
    public Erp101001VO erp101001p1PostCdDcCd(Erp101001VO erp101001VO) {
        erp101001VO = erp101001Mapper.erp101001p1PostCdDcCd(erp101001VO);
        return erp101001VO;
    }

    //체크한것들 삭제(일괄삭제)
    public Erp101001VO erp101001ChkDel(Erp101001VO erp101001VO) {
        //20220517 정연호 kpp오더 라면 조회해서 api로 보낼 값 추출하기
        if (erp101001VO.getOutCmpyCd().equals("KPP")) {
            log.info("체크한것 일괄삭제 - 주문상태 변경 - kpp 오더입니다.");
            ErpCommonVO erpCommonVO = new ErpCommonVO();
            erpCommonVO.setSoNoList(erp101001VO.getSoNo());
            erpCommonVO.setSoStatCd("0000");        //0000 삭제
            erpCommonVO.setDelResn(erp101001VO.getDelResn());
            erpCommonVO = erpCommonMapper.erpCommoSendValueSearch(erpCommonVO);
            log.info(erpCommonVO.toString());
            //20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
            if (erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals("")) {
                //20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다
                erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
                if (erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y")) {
                    erp101001VO = erp101001Mapper.erp101001ChkDel(erp101001VO);
                } else {
                    erp101001VO.setRtnYn("N");
                    erp101001VO.setRtnMsg("일괄삭제 - KPP API 오더상태 값 변경 실패");
                }
            }
        } else {
            erp101001VO = erp101001Mapper.erp101001ChkDel(erp101001VO);
        }
        return erp101001VO;
    }
}