package com.sonictms.alsys.hyapp.manage.service;

import com.sonictms.alsys.hyapp.manage.mapper.HyappCheckMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class HyappCheckService {

    private final HyappCheckMapper hyappCheckMapper;

    public HyappCheckService(HyappCheckMapper hyappCheckMapper) {
        this.hyappCheckMapper = hyappCheckMapper;
    }
    // 전산 재고 조회
    public List<Map<String, Object>> getSystemStock(String locCd) {
        return hyappCheckMapper.selectSystemStock(locCd);
    }

    // 실사 저장 (트랜잭션 처리)
    @Transactional
    public void saveCheck(Map<String, Object> params) {
        hyappCheckMapper.mergeStockTake(params);
    }

    public List<Map<String, Object>> getCheckHistorySummary() {
        return hyappCheckMapper.selectCheckHistorySummary();
    }

    // 현재 로케이션 실사 내역
    public List<Map<String, Object>> getCheckList(String locCd) {
        return hyappCheckMapper.selectCheckList(locCd);
    }

    // 상품 정보
    public Map<String, Object> getProductInfo(String barcode) {
        return hyappCheckMapper.selectProductInfo(barcode);
    }

    // 일자별 이력
    public List<Map<String, Object>> getCheckHistory(String date) {
        return hyappCheckMapper.selectCheckHistory(date);
    }
}