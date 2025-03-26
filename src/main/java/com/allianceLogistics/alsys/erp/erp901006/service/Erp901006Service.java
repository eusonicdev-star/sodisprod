package com.allianceLogistics.alsys.erp.erp901006.service;

import com.allianceLogistics.alsys.erp.erp901006.entity.Erp901006VO;
import com.allianceLogistics.alsys.erp.erp901006.mapper.Erp901006Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp901006Service {

    private final Erp901006Mapper erp901006Mapper;

    //그리드 불러오기
    public List<Erp901006VO> erp901006List(Erp901006VO erp901006VO) {
        List<Erp901006VO> list = erp901006Mapper.erp901006List(erp901006VO);
        return list;
    }

    //양식 저장/수정하기
    public Erp901006VO erp901006p1Save(Erp901006VO erp901006VO) {
        erp901006VO = erp901006Mapper.erp901006p1Save(erp901006VO);
        return erp901006VO;
    }

    //수정팝업에서 내용 조회하기
    public Erp901006VO erp901006p2List(Erp901006VO erp901006VO) {
        erp901006VO = erp901006Mapper.erp901006p2List(erp901006VO);
        return erp901006VO;
    }
}