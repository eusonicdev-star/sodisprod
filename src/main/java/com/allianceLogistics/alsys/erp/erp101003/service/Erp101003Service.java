package com.allianceLogistics.alsys.erp.erp101003.service;

import com.allianceLogistics.alsys.common.entity.ErpCommonVO;
import com.allianceLogistics.alsys.common.mapper.ErpCommonMapper;
import com.allianceLogistics.alsys.common.service.ErpCommonService;
import com.allianceLogistics.alsys.erp.erp101003.entity.Erp101003VO;
import com.allianceLogistics.alsys.erp.erp101003.mapper.Erp101003Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp101003Service {

    private final Erp101003Mapper erp101003Mapper;
    private final ErpCommonMapper erpCommonMapper;
    private final ErpCommonService erpCommonService;

    // 그리드 불러오기
    public List<Erp101003VO> erp101003List(Erp101003VO erp101003VO) {
        return erp101003Mapper.erp101003List(erp101003VO);
    }

    // 체크한것들 삭제(일괄삭제)
    public Erp101003VO erp101003ChkDel(Erp101003VO erp101003VO) {
        //20220517 정연호 kpp오더 라면 조회해서 api로 보낼 값 추출하기
        if (erp101003VO.getOutCmpyCd().equals("KPP")) {
            log.info("체크한것 일괄 삭제 - kpp 오더입니다.");
            ErpCommonVO erpCommonVO = new ErpCommonVO();
            erpCommonVO.setSoNoList(erp101003VO.getSoNo());
            erpCommonVO.setSoStatCd("0000");
            erpCommonVO.setDelResn(erp101003VO.getDelResn());
            erpCommonVO = erpCommonMapper.erpCommoSendValueSearch(erpCommonVO);
            //20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
            if (erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals("")) {
                //20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다
                erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
                if (erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y")) {
                    erp101003VO = erp101003Mapper.erp101003ChkDel(erp101003VO);
                } else {
                    erp101003VO.setRtnYn("N");
                    erp101003VO.setRtnMsg("일괄삭제 - KPP API 오더상태 값 변경 실패");
                }
            }
        } else {
            erp101003VO = erp101003Mapper.erp101003ChkDel(erp101003VO);
        }
        return erp101003VO;
    }

    // 체크한것들 변경(일괄변경)
    public Erp101003VO erp101003chkUpdt(Erp101003VO erp101003VO) {
        //20220517 정연호 kpp오더 라면 조회해서 api로 보낼 값 추출하기
        if (erp101003VO.getOutCmpyCd().equals("KPP") && erp101003VO.getUpdtGubn().equals("1")) {
            log.info("체크한것 일괄변경 - 주문상태 변경 - kpp 오더입니다.");
            ErpCommonVO erpCommonVO = new ErpCommonVO();
            erpCommonVO.setSoNoList(erp101003VO.getSoNo());
            erpCommonVO.setSoStatCd(erp101003VO.getSoStatCd());
            erpCommonVO.setDelResn(erp101003VO.getDelResn());
            erpCommonVO = erpCommonMapper.erpCommoSendValueSearch(erpCommonVO);
            //20220517 정연호 kpp오더인지 조회했는게 오더아이디가 있으면 kpp오더임
            if (erpCommonVO != null && erpCommonVO.getDlvOrdId() != null && !erpCommonVO.getDlvOrdId().trim().equals("")) {
                //20210517 정연호 kpp오더라면 api를 수행해서 kpp쪽 상태값을 변경해준다
                erpCommonVO = erpCommonService.kppDlvStateChange(erpCommonVO);
                if (erpCommonVO != null && erpCommonVO.response != null && erpCommonVO.response.getIfYn() != null && erpCommonVO.response.getIfYn().equals("Y")) {
                    erp101003VO = erp101003Mapper.erp101003chkUpdt(erp101003VO);
                } else {
                    erp101003VO.setRtnYn("N");
                    erp101003VO.setRtnMsg("일괄변경 실패 - KPP API 오더상태 값 변경 실패");
                }
            }
        } else {
            erp101003VO = erp101003Mapper.erp101003chkUpdt(erp101003VO);
        }
        return erp101003VO;
    }

    //20211226 정연호 추가.  체크한것들 주문복사
    public Erp101003VO erp101003chkCopy(Erp101003VO erp101003VO) {
        erp101003VO = erp101003Mapper.erp101003chkCopy(erp101003VO);
        return erp101003VO;
    }

}