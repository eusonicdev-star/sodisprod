package com.sonictms.alsys.erp.erp102003.service;

import com.sonictms.alsys.erp.erp102003.entity.Erp102003VO;
import com.sonictms.alsys.erp.erp102003.mapper.Erp102003Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp102003Service {

    private final Erp102003Mapper erp102003Mapper;

    //리스트조회 그리드 불러오기
    public List<Erp102003VO> erp102003List(Erp102003VO erp102003VO) {
        List<Erp102003VO> list = erp102003Mapper.erp102003List(erp102003VO);
        return list;
    }
}
