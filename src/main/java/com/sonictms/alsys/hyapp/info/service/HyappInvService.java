package com.sonictms.alsys.hyapp.info.service;

import com.sonictms.alsys.hyapp.info.mapper.HyappInvMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HyappInvService {

    // 1. Mapper 필드 선언
    private final HyappInvMapper hyappInvMapper;

    // 2. 생성자 주입 (Spring이 자동으로 Mapper 구현체를 넣어줍니다)
    public HyappInvService(HyappInvMapper hyappInvMapper) {
        this.hyappInvMapper = hyappInvMapper;
    }

    public List<Map<String, Object>> getProductList(String keyword) {
        String kw = (keyword == null) ? "" : keyword.trim();
        if (kw.isEmpty()) {
            return hyappInvMapper.selectProductList(null, false, null);
        }

        String normalized = kw.replaceAll("\\s+", " ");
        boolean hasSpace = normalized.contains(" ");

        List<String> tokens = null;
        if (hasSpace) {
            tokens = Arrays.stream(normalized.split(" "))
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .collect(Collectors.toList());
        }

        return hyappInvMapper.selectProductList(normalized, hasSpace, tokens);
    }

    /**
     * 로케이션 상세 조회 (하단 리스트)
     * - 상단에서 선택한 mtrlCd로 조회
     */
    public List<Map<String, Object>> getLocationListByMtrlCd(String mtrlCd) {
        String cd = (mtrlCd == null) ? "" : mtrlCd.trim();
        if (cd.isEmpty()) return Collections.emptyList();
        return hyappInvMapper.selectLocationListByMtrlCd(cd);
    }

    // [추가] 랙 목록 조회
    public List<Map<String, Object>> getRackList(String keyword, boolean emptyOnly) {
        return hyappInvMapper.selectRackList(keyword, emptyOnly);
    }

    // [추가] 랙 내부 상품 상세
    public List<Map<String, Object>> getRackItemList(String locId, String locCd) {
        return hyappInvMapper.selectRackItemList(locId, locCd);
    }

    public List<Map<String, Object>> getGlobalStockList() {
        return hyappInvMapper.selectGlobalStockList();
    }

    public List<Map<String, Object>> getCustLocationList(String custCd, String mtrlCd) {
        return hyappInvMapper.selectCustLocationList(custCd, mtrlCd);
    }
}