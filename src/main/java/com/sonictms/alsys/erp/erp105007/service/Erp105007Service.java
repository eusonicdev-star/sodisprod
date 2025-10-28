package com.sonictms.alsys.erp.erp105007.service;

import com.sonictms.alsys.erp.erp105007.entity.Erp105007VO;
import com.sonictms.alsys.erp.erp105007.mapper.Erp105007Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Erp105007Service {

    @Autowired
    private Erp105007Mapper erp105007Mapper;

    public List<Erp105007VO> erp105007List(Erp105007VO erp105007VO) {
        return erp105007Mapper.erp105007List(erp105007VO);
    }
}
