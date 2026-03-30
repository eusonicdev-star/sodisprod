package com.sonictms.alsys.erp.erp104001.service;


import com.sonictms.alsys.erp.erp104001.entity.Erp104001VO;
import com.sonictms.alsys.erp.erp104001.mapper.Erp104001Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp104001Service {

    private final Erp104001Mapper erp104001Mapper;

    //그리드 불러오기
    public List<Erp104001VO> erp104001List(Erp104001VO erp104001VO) {
        List<Erp104001VO> list = erp104001Mapper.erp104001List(erp104001VO);
        return list;
    }
} 