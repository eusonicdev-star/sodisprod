package com.sonictms.alsys.erp.erp105007.service;

import com.sonictms.alsys.erp.erp105007.entity.Erp105007VO;
import com.sonictms.alsys.erp.erp105007.mapper.Erp105007Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class Erp105007Service {

    @Autowired
    private Erp105007Mapper erp105007Mapper;

    public List<Erp105007VO> erp105007List(Erp105007VO erp105007VO) {
        // 날짜가 없으면 자동으로 3개월 이전부터 오늘까지 설정
        if (erp105007VO.getFromDt() == null || erp105007VO.getFromDt().isEmpty() ||
            erp105007VO.getToDt() == null || erp105007VO.getToDt().isEmpty()) {
            LocalDate today = LocalDate.now();
            LocalDate threeMonthsAgo = today.minusMonths(3);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            erp105007VO.setFromDt(threeMonthsAgo.format(formatter));
            erp105007VO.setToDt(today.format(formatter));
        }
        
        // 회사코드가 없으면 기본값 설정
        if (erp105007VO.getCmpyCd() == null || erp105007VO.getCmpyCd().isEmpty()) {
            erp105007VO.setCmpyCd("A");
        }
        
        return erp105007Mapper.erp105007List(erp105007VO);
    }
}
