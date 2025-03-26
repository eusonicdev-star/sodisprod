package com.allianceLogistics.alsys.erp.erp203003.service;

import com.allianceLogistics.alsys.erp.erp203003.entity.Erp203003VO;
import com.allianceLogistics.alsys.erp.erp203003.mapper.Erp203003Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp203003Service {

    private final Erp203003Mapper erp203003Mapper;

    //메인화면조회
    public List<Erp203003VO> erp203003List(Erp203003VO erp203003VO) {
        List<Erp203003VO> list = erp203003Mapper.erp203003List(erp203003VO);
        return list;
    }

    //응답비율 조회
    public List<Erp203003VO> erp203003List2(Erp203003VO erp203003VO) {
        List<Erp203003VO> list = erp203003Mapper.erp203003List2(erp203003VO);
        return list;
    }
} 