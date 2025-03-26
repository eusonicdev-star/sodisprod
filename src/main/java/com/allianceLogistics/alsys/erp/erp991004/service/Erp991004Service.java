package com.allianceLogistics.alsys.erp.erp991004.service;

import com.allianceLogistics.alsys.erp.erp991004.entity.Erp991004VO;
import com.allianceLogistics.alsys.erp.erp991004.mapper.Erp991004Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp991004Service {

    private final Erp991004Mapper erp991004Mapper;

    //사용자 목록 그리드 리스트 불러오기
    public List<Erp991004VO> erp991004List(Erp991004VO erp991004VO) {
        List<Erp991004VO> list = erp991004Mapper.erp991004List(erp991004VO);
        return list;
    }


} 