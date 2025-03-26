package com.allianceLogistics.alsys.erp.erp991005.service;


import com.allianceLogistics.alsys.erp.erp991005.entity.Erp991005VO;
import com.allianceLogistics.alsys.erp.erp991005.mapper.Erp991005Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp991005Service {

    private final Erp991005Mapper erp991005Mapper;

    //왼쪽 마스터공통 불러오기
    public List<Erp991005VO> erp991005LCommList(Erp991005VO erp991005VO) {
        List<Erp991005VO> list = erp991005Mapper.erp991005LCommList(erp991005VO);
        return list;
    }

    //오른쪽 상세공통 불러오기
    public List<Erp991005VO> erp991005RComdList(Erp991005VO erp991005VO) {
        List<Erp991005VO> list = erp991005Mapper.erp991005RComdList(erp991005VO);
        return list;
    }

    //등록/수정 팝업에서 수정일떄 내용 불러오기
    public Erp991005VO erp991005p1ComdSearch(Erp991005VO erp991005VO) {
        erp991005VO = erp991005Mapper.erp991005p1ComdSearch(erp991005VO);
        return erp991005VO;
    }

    //등록/수정 팝업에서 저장하기
    public Erp991005VO erp991005p1ComdSave(Erp991005VO erp991005VO) {
        erp991005VO = erp991005Mapper.erp991005p1ComdSave(erp991005VO);
        return erp991005VO;
    }

} 