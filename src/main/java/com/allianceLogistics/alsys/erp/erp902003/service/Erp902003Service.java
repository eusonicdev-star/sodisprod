package com.allianceLogistics.alsys.erp.erp902003.service;


import com.allianceLogistics.alsys.erp.erp902003.entity.Erp902003VO;
import com.allianceLogistics.alsys.erp.erp902003.mapper.Erp902003Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp902003Service {

    private final Erp902003Mapper erp902003Mapper;

    //그리드 불러오기
    public List<Erp902003VO> erp902003List(Erp902003VO erp902003VO) {
        List<Erp902003VO> list = erp902003Mapper.erp902003List(erp902003VO);
        return list;
    }

    //권역매핑저장하기
    public Erp902003VO erp902003Save(Erp902003VO erp902003VO) {
        erp902003VO = erp902003Mapper.erp902003Save(erp902003VO);
        return erp902003VO;
    }

    //권역매핑삭제하기
    public Erp902003VO erp902003Delete(Erp902003VO erp902003VO) {
        erp902003VO = erp902003Mapper.erp902003Delete(erp902003VO);
        return erp902003VO;
    }

    //권역 등록 수정 팝업의 중복확인하기
    public Erp902003VO erp902003p1DoubleChk(Erp902003VO erp902003VO) {
        erp902003VO = erp902003Mapper.erp902003p1DoubleChk(erp902003VO);
        return erp902003VO;
    }

    //권역 등록 수정 팝업의 등록/수정 하기
    public Erp902003VO erp902003p1Save(Erp902003VO erp902003VO) {
        erp902003VO = erp902003Mapper.erp902003p1Save(erp902003VO);
        return erp902003VO;
    }


} 