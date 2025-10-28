package com.sonictms.alsys.erp.erp105008.service;

import com.sonictms.alsys.erp.erp105008.entity.Erp105008VO;
import com.sonictms.alsys.erp.erp105008.mapper.Erp105008Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp105008Service {

    private final Erp105008Mapper erp105008Mapper;

    public List<Erp105008VO> erp105008List(Erp105008VO erp105008VO) {
        return erp105008Mapper.erp105008List(erp105008VO);
    }
}
