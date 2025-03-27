package com.sonictms.alsys.erp.erp902006.service;

import com.sonictms.alsys.erp.erp902006.entity.Erp902006VO;
import com.sonictms.alsys.erp.erp902006.mapper.Erp902006Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp902006Service {

    private final Erp902006Mapper erp902006Mapper;

    //그리드 불러오기
    public List<Erp902006VO> erp902006List(Erp902006VO erp902006VO) {
        List<Erp902006VO> list = erp902006Mapper.erp902006List(erp902006VO);
        return list;
    }


    //일괄저장 하기 버튼
    public Erp902006VO erp902006Save(Erp902006VO erp902006VO) {
        erp902006VO = erp902006Mapper.erp902006Save(erp902006VO);
        return erp902006VO;
    }
}