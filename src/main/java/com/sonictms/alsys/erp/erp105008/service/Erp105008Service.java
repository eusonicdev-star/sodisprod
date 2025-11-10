package com.sonictms.alsys.erp.erp105008.service;

import com.sonictms.alsys.erp.erp105008.entity.Erp105008VO;
import com.sonictms.alsys.erp.erp105008.mapper.Erp105008Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class Erp105008Service {

    @Autowired
    private Erp105008Mapper erp105008Mapper;

    public List<Erp105008VO> erp105008List(Erp105008VO erp105008VO) {
        // 날짜가 없으면 자동으로 3개월 이전부터 오늘까지 설정
        if (erp105008VO.getFromDt() == null || erp105008VO.getFromDt().isEmpty() ||
            erp105008VO.getToDt() == null || erp105008VO.getToDt().isEmpty()) {
            LocalDate today = LocalDate.now();
            LocalDate threeMonthsAgo = today.minusMonths(3);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            erp105008VO.setFromDt(threeMonthsAgo.format(formatter));
            erp105008VO.setToDt(today.format(formatter));
        }
        
        // 회사코드가 없으면 기본값 설정
        if (erp105008VO.getCmpyCd() == null || erp105008VO.getCmpyCd().isEmpty()) {
            erp105008VO.setCmpyCd("A");
        }
        
        return erp105008Mapper.erp105008List(erp105008VO);
    }
}
