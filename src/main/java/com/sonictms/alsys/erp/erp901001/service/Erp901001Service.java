package com.sonictms.alsys.erp.erp901001.service;

import com.sonictms.alsys.erp.erp901001.entity.Erp901001DetailVO;
import com.sonictms.alsys.erp.erp901001.entity.Erp901001VO;
import com.sonictms.alsys.erp.erp901001.mapper.Erp901001Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp901001Service {

    private final Erp901001Mapper erp901001Mapper;

    //그리드 불러오기
    public List<Erp901001VO> erp901001List(Erp901001VO erp901001VO) {
        List<Erp901001VO> list = erp901001Mapper.erp901001List(erp901001VO);
        return list;
    }

    //고유 ID 로 정보 찾기
    public Erp901001VO erp901001p1agencySrch(Erp901001VO erp901001VO) {
        erp901001VO = erp901001Mapper.erp901001p1agencySrch(erp901001VO);
        return erp901001VO;
    }


    //팝업 화주 등록/ 수정 하기
    public Erp901001VO erp901001p1ComdSave(Erp901001VO erp901001VO) {
        erp901001VO = erp901001Mapper.erp901001p1ComdSave(erp901001VO);
        return erp901001VO;
    }

    //20220124 정연호 알림톡 연락처 용 화주사 저장/수정하기
    public Erp901001VO erp901001p2ComdSave(Erp901001VO erp901001VO) {
        String saveGubn = erp901001VO.getSaveGubn();
        String saveUser = erp901001VO.getSaveUser();
        String cmpyCd = erp901001VO.getCmpyCd();
        Erp901001VO resultVO = erp901001Mapper.erp901001p1ComdSave(erp901001VO);    //알림톡 메인정보를 입력하고 saveGubn 이 INS입력일때만 화주사코드를 리턴받는다

        if (saveGubn.equals("INS"))    //신규입력일 경우
        {
            String agntCd = resultVO.getAgntCd();    //리턴받은 결과값 중에 있는 agntCd
            for (int i = 0; i < erp901001VO.getAlrmTlkDetail().size(); i++) {
                erp901001VO.getAlrmTlkDetail().get(i).setSaveGubn(saveGubn);
                erp901001VO.getAlrmTlkDetail().get(i).setAgntCd(agntCd);
                erp901001VO.getAlrmTlkDetail().get(i).setSaveUser(saveUser);
                erp901001VO.getAlrmTlkDetail().get(i).setCmpyCd(cmpyCd);
                Erp901001DetailVO detailVO = erp901001Mapper.erp901001p2DetailSave(erp901001VO.getAlrmTlkDetail().get(i));
            }
        } else if (saveGubn.equals("UPDT"))    //수정일 경우
        {
            String agntCd = erp901001VO.getAgntCd();    //리턴받은 결과값 중에 있는 agntCd
            for (int i = 0; i < erp901001VO.getAlrmTlkDetail().size(); i++) {
                if (erp901001VO.getAlrmTlkDetail().get(i).getCud().equals("C")) {
                    saveGubn = "INS";
                } else if (erp901001VO.getAlrmTlkDetail().get(i).getCud().equals("U")) {
                    saveGubn = "UPDT";
                }
                erp901001VO.getAlrmTlkDetail().get(i).setSaveGubn(saveGubn);
                erp901001VO.getAlrmTlkDetail().get(i).setAgntCd(agntCd);
                erp901001VO.getAlrmTlkDetail().get(i).setSaveUser(saveUser);
                erp901001VO.getAlrmTlkDetail().get(i).setCmpyCd(cmpyCd);
                Erp901001DetailVO detailVO = erp901001Mapper.erp901001p2DetailSave(erp901001VO.getAlrmTlkDetail().get(i));
            }
        }
        return resultVO;
    }


    //20220124 정연호. 화주사 코드로 알림톡 연락처 조회하기
    public List<Erp901001DetailVO> erp901001p2alrmTlkSrch(Erp901001DetailVO erp901001DetailVO) {
        List<Erp901001DetailVO> list = erp901001Mapper.erp901001p2alrmTlkSrch(erp901001DetailVO);
        return list;
    }
} 