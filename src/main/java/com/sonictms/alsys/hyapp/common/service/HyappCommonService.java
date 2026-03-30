package com.sonictms.alsys.hyapp.common.service;

import com.sonictms.alsys.hyapp.common.mapper.HyappCommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class HyappCommonService {

    @Autowired
    private HyappCommonMapper hyappCommonMapper;

    /**
     * 물류센터 목록 조회
     * @param cmpyCd 회사코드
     * @return 센터코드 및 센터명 리스트
     */
    public List<Map<String, Object>> getDcList(String cmpyCd) {
        Map<String, Object> params = new HashMap<>();

        // [수정] cmpyCd 값이 없거나 공백이면 'A'로 설정
        if (cmpyCd == null || cmpyCd.trim().isEmpty()) {
            cmpyCd = "A";
        }

        params.put("cmpyCd", cmpyCd);
        return hyappCommonMapper.selectDcList(params);
    }
}