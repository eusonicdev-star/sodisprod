package com.sonictms.alsys.erp.erp202001.service;

import com.sonictms.alsys.erp.erp202001.entity.Erp202001VO;
import com.sonictms.alsys.erp.erp202001.mapper.Erp202001Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp202001Service {

    private final Erp202001Mapper erp202001Mapper;

    //그리드 불러오기
    public List<Erp202001VO> erp202001List(Erp202001VO erp202001VO) {
        List<Erp202001VO> list = erp202001Mapper.erp202001List(erp202001VO);
        return list;
    }


    //erp202001p2 좌석 점유 상제 조회 리스트
    public List<Erp202001VO> erp202001p2List(Erp202001VO erp202001VO) {
        List<Erp202001VO> list = erp202001Mapper.erp202001p2List(erp202001VO);
        return list;
    }


    //시공좌석일괄생성 팝업창에서 일괄생성 저장하기
    public Erp202001VO erp202001p1Save(Erp202001VO erp202001VO) {
        erp202001VO = erp202001Mapper.erp202001p1Save(erp202001VO);
        return erp202001VO;
    }

    //체크수정 하기 버튼
    public Erp202001VO erp202001Save(Erp202001VO erp202001VO) {
        erp202001VO = erp202001Mapper.erp202001Save(erp202001VO);
        return erp202001VO;
    }


    //좌석 마감하기
    public Erp202001VO erp202001End(Erp202001VO erp202001VO) {
        erp202001VO = erp202001Mapper.erp202001End(erp202001VO);
        return erp202001VO;
    }


} 