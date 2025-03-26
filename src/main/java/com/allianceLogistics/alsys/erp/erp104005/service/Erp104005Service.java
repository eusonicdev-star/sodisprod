package com.allianceLogistics.alsys.erp.erp104005.service;

import com.allianceLogistics.alsys.erp.erp104005.entity.Erp104005VO;
import com.allianceLogistics.alsys.erp.erp104005.mapper.Erp104005Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp104005Service {

    private final Erp104005Mapper erp104005Mapper;

    // 그리드 불러오기
    public List<Erp104005VO> erp104005List(Erp104005VO erp104005VO) {
        List<Erp104005VO> list = erp104005Mapper.erp104005List(erp104005VO);
        return list;
    }
}