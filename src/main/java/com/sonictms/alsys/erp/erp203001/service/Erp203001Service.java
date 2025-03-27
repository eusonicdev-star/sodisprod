package com.sonictms.alsys.erp.erp203001.service;

import com.sonictms.alsys.erp.erp203001.entity.Erp203001VO;
import com.sonictms.alsys.erp.erp203001.mapper.Erp203001Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp203001Service {

    private final Erp203001Mapper erp203001Mapper;

    // 그리드 불러오기
    public List<Erp203001VO> erp203001List(Erp203001VO erp203001VO) {
        List<Erp203001VO> list = erp203001Mapper.erp203001List(erp203001VO);
        return list;
    }

    // 리스트조회2 - 아래쪽 상세 그리드 조회
    public List<Erp203001VO> erp203001List2(Erp203001VO erp203001VO) {
        List<Erp203001VO> list = erp203001Mapper.erp203001List2(erp203001VO);
        return list;
    }

    // 시공기사 찾기 조회
    public List<Erp203001VO> erp203001InstErSrch(Erp203001VO erp203001VO) {
        List<Erp203001VO> list = erp203001Mapper.erp203001InstErSrch(erp203001VO);
        return list;
    }

    // 시공기사 찾기 조회2
    public List<Erp203001VO> erp203001InstErSrch2(Erp203001VO erp203001VO) {
        List<Erp203001VO> list = erp203001Mapper.erp203001InstErSrch2(erp203001VO);
        return list;
    }

    // 시공기사 현황 팝업3
    public List<Erp203001VO> erp203001InstErSrch3(Erp203001VO erp203001VO) {
        List<Erp203001VO> list = erp203001Mapper.erp203001InstErSrch3(erp203001VO);
        return list;
    }

    // 그리드에서 순번, 팔렛트, 시공기사 입력한것 저장 또는 수정 하기
    public Erp203001VO erp203001Save(Erp203001VO erp203001VO) {
        erp203001VO = erp203001Mapper.erp203001Save(erp203001VO);
        return erp203001VO;
    }

    // 체크한것들 삭제(일괄삭제)
    public Erp203001VO erp203001DelChk(Erp203001VO erp203001VO) {
        erp203001VO = erp203001Mapper.erp203001DelChk(erp203001VO);
        return erp203001VO;
    }
}