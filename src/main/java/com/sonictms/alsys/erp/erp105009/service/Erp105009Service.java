package com.sonictms.alsys.erp.erp105009.service;

import com.sonictms.alsys.erp.erp105009.entity.Erp105009VO;
import com.sonictms.alsys.erp.erp105009.mapper.Erp105009Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class Erp105009Service {

    @Autowired
    private Erp105009Mapper erp105009Mapper;

    public List<Erp105009VO> erp105009List(Erp105009VO erp105009VO) {
        // 날짜가 없으면 자동으로 3개월 이전부터 오늘까지 설정
        if (erp105009VO.getFromDt() == null || erp105009VO.getFromDt().isEmpty() ||
            erp105009VO.getToDt() == null || erp105009VO.getToDt().isEmpty()) {
            LocalDate today = LocalDate.now();
            LocalDate threeMonthsAgo = today.minusMonths(3);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            erp105009VO.setFromDt(threeMonthsAgo.format(formatter));
            erp105009VO.setToDt(today.format(formatter));
        }
        
        // 회사코드가 없으면 기본값 설정
        if (erp105009VO.getCmpyCd() == null || erp105009VO.getCmpyCd().isEmpty()) {
            erp105009VO.setCmpyCd("A");
        }
        
        return erp105009Mapper.erp105009List(erp105009VO);
    }
}












