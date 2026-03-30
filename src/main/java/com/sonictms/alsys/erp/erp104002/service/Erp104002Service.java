package com.sonictms.alsys.erp.erp104002.service;

import com.sonictms.alsys.erp.erp104002.entity.Erp104002VO;
import com.sonictms.alsys.erp.erp104002.mapper.Erp104002Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp104002Service {

    private final Erp104002Mapper erp104002Mapper;

    //그리드 불러오기
    public List<Erp104002VO> erp104002List(Erp104002VO erp104002VO) {
        List<Erp104002VO> list = erp104002Mapper.erp104002List(erp104002VO);
        return list;
    }
} 