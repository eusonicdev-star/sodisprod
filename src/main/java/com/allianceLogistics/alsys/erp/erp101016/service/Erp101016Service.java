package com.allianceLogistics.alsys.erp.erp101016.service;

import com.allianceLogistics.alsys.erp.erp101016.entity.Erp101016VO;
import com.allianceLogistics.alsys.erp.erp101016.mapper.Erp101016Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp101016Service {

    private final Erp101016Mapper erp101016Mapper;

    // 그리드 불러오기
    public List<Erp101016VO> erp101016List(Erp101016VO erp101016VO) {
        List<Erp101016VO> list = erp101016Mapper.erp101016List(erp101016VO);
        return list;
    }

    // 조치이력 상세보기 팝업의 그리드 리스트 조회하기
    public List<Erp101016VO> erp101016p3List(Erp101016VO erp101016VO) {
        List<Erp101016VO> list = erp101016Mapper.erp101016p3List(erp101016VO);
        return list;
    }

    // 취할 조치 없음 팝업의 예 또는 아니오 버튼을 눌러 값을 저장할때 수행함
    public Erp101016VO erp101016p6Save(Erp101016VO erp101016VO) {
        for (int i = 0; i < erp101016VO.getMidValueArray().length; i++) {
            Erp101016VO vo = new Erp101016VO();
            vo.setTblSoMId(erp101016VO.getMidValueArray()[i]);
            vo.setMissCloseType(erp101016VO.getMissCloseType());
            vo.setSaveUser(erp101016VO.getSaveUser());
            vo = erp101016Mapper.erp101016p6Save(vo);
        }
        return erp101016VO;
    }

    // 반품등록 팝업에서 헤더 불러오기 //헤더는 1줄만 불러옴
    public Erp101016VO erp101016p1HeaderList(Erp101016VO erp101016VO) {
        erp101016VO = erp101016Mapper.erp101016p1HeaderList(erp101016VO);
        return erp101016VO;
    }

    // 반품등록 팝업에서 상세 불러오기 //상세는 리스트
    public List<Erp101016VO> erp101016p1DetailList(Erp101016VO erp101016VO) {
        List<Erp101016VO> list = erp101016Mapper.erp101016p1DetailList(erp101016VO);
        return list;
    }

    // 미마감 주문 저장하기
    public Erp101016VO erp101016p1Save(Erp101016VO erp101016VO) {
        // 1. 헤더 저장
        Erp101016VO returnHeaderVo = erp101016Mapper.erp101016p1HeaderSave(erp101016VO);
        // 2. 헤더에서 리턴 받은 고유 아이디 와 AL 오더
        log.error("미마감 주문 저장 실패 - 사용자 ID: " + erp101016VO.getSaveUser());
        log.error("미마감 주문 저장 실패 - 주문 고유 ID: " + returnHeaderVo.getTblSoMId());
        log.error("미마감 주문 저장 실패 - 주문 번호: " + returnHeaderVo.getSoNo());

        // 3. 미미감/상태 등록
        returnHeaderVo.setOrgnSoMId(erp101016VO.getOrgnSoMId());
        returnHeaderVo.setSoType(erp101016VO.getSoType());
        returnHeaderVo.setMissCloseType(erp101016VO.getMissCloseType());
        returnHeaderVo.setMissCloseStep(erp101016VO.getMissCloseStep());
        returnHeaderVo.setSaveUser(erp101016VO.getSaveUser());

        log.error("미마감 주문 저장 실패 - 원 AL 오더 고유ID: " + returnHeaderVo.getOrgnSoMId());
        log.error("미마감 주문 저장 실패 - 신규 AL 오더 고유ID: " + returnHeaderVo.getTblSoMId());
        log.error("미마감 주문 저장 실패 - 오더유형: " + returnHeaderVo.getSoType());
        log.error("미마감 주문 저장 실패 - 처리유형: " + returnHeaderVo.getMissCloseType());
        log.error("미마감 주문 저장-상태-처리단계       :  " + returnHeaderVo.getMissCloseStep());
        log.error("미마감 주문 저장-상태-저장자        :  " + returnHeaderVo.getSaveUser());

        Erp101016VO returnMissVo = erp101016Mapper.erp101016p1MissSave(returnHeaderVo);

        //헤더입력에서 리턴받은 값이 있어야하고 미마감 상태 입력에서 리턴받은 값이 있어야 상세입력을 한다
        if (returnMissVo != null &&
                returnMissVo.getRtnYn().equals("Y") &&
                returnHeaderVo != null &&
                returnHeaderVo.getTblSoMId() != null &&
                !returnHeaderVo.getTblSoMId().equals("") &&
                returnHeaderVo.getSoNo() != null &&
                !returnHeaderVo.getSoNo().equals("")
        ) {
            // 4. 헤더저장에서 반환받은 TBL_SO_M_ID 와 SO_NO 로 상세 저장
            for (int i = 0; i < erp101016VO.getDetailData().size(); i++) {

                String saveUser = erp101016VO.getSaveUser();
                String tblSoMId = returnHeaderVo.getTblSoMId();
                String soNo = returnHeaderVo.getSoNo(); // 헤더에서 넘겨받은 오더번호
                String prodSeq = erp101016VO.getDetailData().get(i).getProdSeq();

                String rqstDt = erp101016VO.getRqstDt();
                String dlvyCnfmDt = erp101016VO.getDlvyCnfmDt();
                String dcCd = erp101016VO.getDcCd();

                String agntCd = erp101016VO.getAgntCd(); // 20211103 정연호 추가. 화주사 코드
                erp101016VO.getDetailData().get(i).setAgntCd(agntCd);// 20211103 정연호 추가. 화주사 코드

                erp101016VO.getDetailData().get(i).setSaveUser(saveUser);
                erp101016VO.getDetailData().get(i).setSaveUser(saveUser);
                erp101016VO.getDetailData().get(i).setTblSoMId(tblSoMId);
                erp101016VO.getDetailData().get(i).setSoNo(soNo);

                erp101016VO.getDetailData().get(i).setRqstDt(rqstDt);
                erp101016VO.getDetailData().get(i).setDlvyCnfmDt(dlvyCnfmDt);
                erp101016VO.getDetailData().get(i).setDcCd(dcCd);
                erp101016VO.getDetailData().get(i).setUseYn("Y");

                if (erp101016VO.getDetailData().get(i).getRcptCost() != null &&
                        !erp101016VO.getDetailData().get(i).getRcptCost().equals("0") &&
                        !erp101016VO.getDetailData().get(i).getRcptCost().equals("")
                ) {
                    erp101016VO.getDetailData().get(i).setDlvyCostType(erp101016VO.getDlvyCostType());
                }

                erp101016VO.getDetailData().set(i, erp101016Mapper.erp101016p1DetailSave(erp101016VO.getDetailData().get(i)));
                erp101016VO.getDetailData().get(i).setTblSoMId(tblSoMId);
                erp101016VO.getDetailData().get(i).setSoNo(soNo);
                erp101016VO.getDetailData().get(i).setSaveUser(saveUser);
                erp101016VO.getDetailData().get(i).setProdSeq(prodSeq);
            }

            // 반복 돌려서 결과중에 Y 가 아닌게 1개라도 있으면 트랜잭션 강제 롤백
            int errCnt = 0;
            for (int i = 0; i < erp101016VO.getDetailData().size(); i++) {
                if (!erp101016VO.getDetailData().get(i).getRtnYn().equals("Y")) {
                    errCnt++;
                }
            }
            if (errCnt > 0) // 에러가 있으면
            {
                // 헤더에 리턴을 실패라고 보냄
                returnHeaderVo.setRtnYn("N");
                returnHeaderVo.setRtnMsg("미마감주문 상세입력 실패");

                // 강제롤백
                log.info("미마감주문 상세입력 실패 - 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } else {    // 에러없이 정상이면
                returnHeaderVo.setDetailData(erp101016VO.getDetailData());
            }
        }
        // 헤더 입력의 결과 (고유ID,AL 오더) 안넘어 왔다면 강제로 롤백
        else {
            //미마감 상태입력이 오류
            if (returnMissVo == null || (returnMissVo != null && !returnMissVo.getRtnYn().equals("Y"))) {
                returnHeaderVo.setRtnYn("N");
                returnHeaderVo.setRtnMsg("미마감주문 상태입력 실패");
                // 강제롤백
                log.info("미마감주문 상태입력 실패 - 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } else {
                returnHeaderVo.setRtnYn("N");
                returnHeaderVo.setRtnMsg("미마감주문 헤더입력 실패");
                // 강제롤백
                log.info("미마감주문 헤더입력 실패 - 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        return returnHeaderVo;
    }

    /// 우편번호로 물류센터 찾기
    public Erp101016VO erp101016p1PostCdDcCd(Erp101016VO erp101016VO) {
        erp101016VO = erp101016Mapper.erp101016p1PostCdDcCd(erp101016VO);
        return erp101016VO;
    }

    // 배송확정일 가능한 날짜 조회
    public List<Erp101016VO> erp101016p1List(Erp101016VO erp101016VO) {
        List<Erp101016VO> list = erp101016Mapper.erp101016p1List(erp101016VO);
        return list;
    }

    // 원오더찾기 팝업에서 리스트조회
    public List<Erp101016VO> erp101016p0List(Erp101016VO erp101016VO) {
        List<Erp101016VO> list = erp101016Mapper.erp101016p0List(erp101016VO);
        return list;
    }
}