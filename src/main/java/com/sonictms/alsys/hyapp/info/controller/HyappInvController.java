package com.sonictms.alsys.hyapp.info.controller;

import com.sonictms.alsys.hyapp.info.service.HyappInvService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hyapp/api")
public class HyappInvController {

    private final HyappInvService hyappInvService;

    public HyappInvController(HyappInvService hyappInvService) {
        this.hyappInvService = hyappInvService;
    }

    /**
     * 1. 상품 목록 조회 (상단 리스트용)
     * URL: /hyapp/api/inv/products?keyword=...
     * Return: [{custNm, mtrlCd, mtrlNm, qty}, ...]
     */
    @GetMapping("/inv/products")
    public List<Map<String, Object>> getProductList(
            @RequestParam(required = false) String keyword
    ) {
        // 로그 확인용
        System.out.println("API Request [Products] keyword: " + keyword);

        if (StringUtils.isBlank(keyword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "검색조건(keyword)이 없습니다.");
        }

        // 서비스에서 List<Map> 혹은 DTO 리스트를 반환하도록 호출
        return hyappInvService.getProductList(keyword);
    }

    /**
     * 2. 로케이션별 재고 조회 (하단 상세용)
     * URL: /hyapp/api/inv/locs?keyword=...
     * Return: [{mtrlCd, locCd, qty}, ...]
     */
    @GetMapping("/inv/locs")
    public List<Map<String, Object>> getLocs(@RequestParam String mtrlCd) {
        return hyappInvService.getLocationListByMtrlCd(mtrlCd);
    }

    // ============================================================
    // [추가] 로케이션(랙) 재고 조회 화면용 (HyappInvLocation.html)
    // ============================================================

    /**
     * 3. 존/랙 목록 조회 (요약)
     * URL: /hyapp/api/inv/racks?keyword=...&emptyOnly=...
     */
    @GetMapping("/inv/racks")
    public List<Map<String, Object>> getRackList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String emptyOnly
    ) {
        // emptyOnly가 "1" 혹은 "true"이면 true로 처리
        boolean isEmptyOnly = "1".equals(emptyOnly) || "true".equalsIgnoreCase(emptyOnly);
        return hyappInvService.getRackList(keyword, isEmptyOnly);
    }

    /**
     * 4. 특정 랙 내부 상품 상세 조회
     * URL: /hyapp/api/inv/rack/items?locId=... (또는 locCd)
     */
    @GetMapping("/inv/rack/items")
    public List<Map<String, Object>> getRackItemList(
            @RequestParam(required = false) String locId,
            @RequestParam(required = false) String locCd
    ) {
        if (StringUtils.isAllBlank(locId, locCd)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로케이션 정보(locId 또는 locCd)가 필요합니다.");
        }
        return hyappInvService.getRackItemList(locId, locCd);
    }

    // ============================================================
    // [추가] 고객 재고 조회 화면용 (HyappInvCust.html)
    // ============================================================

    /**
     * [화면1] 전체 재고 현황 (고객사+상품별 합계)
     * - 조건 없이 전체 조회 (단, 재고 > 0 인 것만)
     */
    @GetMapping("/inv/stock/all")
    public List<Map<String, Object>> getGlobalStockList() {
        return hyappInvService.getGlobalStockList();
    }

    /**
     * [상세 화면] 특정 고객사 + 특정 상품의 로케이션별 재고 조회
     * URL: /hyapp/api/inv/cust/locations?custCd=...&mtrlCd=...
     */
    @GetMapping("/inv/cust/locations")
    public List<Map<String, Object>> getCustLocationList(
            @RequestParam String custCd,
            @RequestParam String mtrlCd
    ) {
        return hyappInvService.getCustLocationList(custCd, mtrlCd);
    }
}