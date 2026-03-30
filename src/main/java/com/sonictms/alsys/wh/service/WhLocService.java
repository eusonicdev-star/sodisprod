package com.sonictms.alsys.wh.service;

import com.sonictms.alsys.wh.entity.WhLocVO;
import com.sonictms.alsys.wh.mapper.WhLocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WhLocService {
    @Autowired
    private WhLocMapper locMapper;

    public List<WhLocVO> getLocList(Map<String, Object> params) {
        return locMapper.selectLocList(params);
    }

    @Transactional
    public void saveLocation(WhLocVO vo) {
        // 필수 값 설정 (세션 정보 등 활용)
        if (vo.getLocId() == null) {
            vo.setSaveTime(new Date());
            vo.setDeletedYn("N");
        }
        locMapper.saveLocation(vo);
    }
}