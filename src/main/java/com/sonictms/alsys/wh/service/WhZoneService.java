package com.sonictms.alsys.wh.service;

import com.sonictms.alsys.wh.entity.WhZoneVO;
import com.sonictms.alsys.wh.mapper.WhZoneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class WhZoneService {

    @Autowired
    private WhZoneMapper whZoneMapper;

    /**
     * 존 목록 조회
     */
    public List<WhZoneVO> getZoneList(Map<String, Object> params) {
        // 필수 파라미터 체크 (cmpyCd, whCd 등)
        return whZoneMapper.selectZoneList(params);
    }

    /**
     * 존 정보 저장 (등록/수정)
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveZone(WhZoneVO vo) {
        // 저장 전 비즈니스 로직 (예: 사용자 세션 정보 주입 등)
        if (vo.getZoneId() == null) {
            // 신규 등록 시 기본값 설정
            vo.setUseYn("Y");
            vo.setDeletedYn("N");
        }

        whZoneMapper.saveZone(vo);
    }

    /**
     * 존 삭제 (Soft Delete)
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteZone(Long zoneId, String userId) {
        whZoneMapper.deleteZone(zoneId, userId);
    }
}