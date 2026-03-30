package com.sonictms.alsys.erp.erp104003.service;

import com.sonictms.alsys.erp.erp104003.entity.Erp104003VO;
import com.sonictms.alsys.erp.erp104003.mapper.Erp104003Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp104003Service {

    private final Erp104003Mapper erp104003Mapper;

    //그리드 불러오기
    public List<Erp104003VO> erp104003List(Erp104003VO erp104003VO) {
        List<Erp104003VO> list = erp104003Mapper.erp104003List(erp104003VO);
        return list;
    }
}
