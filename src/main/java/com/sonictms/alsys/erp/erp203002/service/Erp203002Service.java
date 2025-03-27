package com.sonictms.alsys.erp.erp203002.service;


import com.sonictms.alsys.erp.erp203002.entity.Erp203002VO;
import com.sonictms.alsys.erp.erp203002.mapper.Erp203002Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp203002Service {

    private final Erp203002Mapper erp203002Mapper;

    //그리드 불러오기
    public List<Erp203002VO> erp203002List(Erp203002VO erp203002VO) {
        List<Erp203002VO> list = erp203002Mapper.erp203002List(erp203002VO);
        return list;
    }

    //체크한것들 확정
    public Erp203002VO erp203002rowCnfm(Erp203002VO erp203002VO) {
        erp203002VO = erp203002Mapper.erp203002rowCnfmCnsl(erp203002VO); //확정/취소 같은 SP
        return erp203002VO;
    }

    //체크한것들 취소
    public Erp203002VO erp203002rowCnsl(Erp203002VO erp203002VO) {
        erp203002VO = erp203002Mapper.erp203002rowCnfmCnsl(erp203002VO);    //확정/취소 같은 SP
        return erp203002VO;
    }

    //모바일확정 일괄 확정/취소 팝업에서 일괄 확정/취소 하기
    public Erp203002VO erp203002allCnfmCnsl(Erp203002VO erp203002VO) {
        erp203002VO = erp203002Mapper.erp203002allCnfmCnsl(erp203002VO);
        return erp203002VO;
    }
}