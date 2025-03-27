package com.sonictms.alsys.erp.erp104004.service;


import com.sonictms.alsys.erp.erp104004.entity.Erp104004VO;
import com.sonictms.alsys.erp.erp104004.mapper.Erp104004Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp104004Service {

    private final Erp104004Mapper erp104004Mapper;

    //그리드 불러오기
    public List<Erp104004VO> erp104004List(Erp104004VO erp104004VO) {
        List<Erp104004VO> list = erp104004Mapper.erp104004List(erp104004VO);
        return list;
    }
} 