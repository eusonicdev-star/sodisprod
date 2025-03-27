package com.sonictms.alsys.erp.erp902004.service;


import com.sonictms.alsys.erp.erp902004.entity.Erp902004VO;
import com.sonictms.alsys.erp.erp902004.mapper.Erp902004Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp902004Service {

    private final Erp902004Mapper erp902004Mapper;

    //그리드 불러오기
    public List<Erp902004VO> erp902004List(Erp902004VO erp902004VO) {
        List<Erp902004VO> list = erp902004Mapper.erp902004List(erp902004VO);
        return list;
    }


    //물류센터매핑저장하기
    public Erp902004VO erp902004Save(Erp902004VO erp902004VO) {
        erp902004VO = erp902004Mapper.erp902004Save(erp902004VO);
        return erp902004VO;
    }

    //물류센터매핑삭제하기
    public Erp902004VO erp902004Delete(Erp902004VO erp902004VO) {
        erp902004VO = erp902004Mapper.erp902004Delete(erp902004VO);
        return erp902004VO;
    }


} 