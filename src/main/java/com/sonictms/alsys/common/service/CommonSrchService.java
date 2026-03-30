package com.sonictms.alsys.common.service;

import com.sonictms.alsys.common.entity.CommonSrchVO;
import com.sonictms.alsys.common.mapper.CommonSrchMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommonSrchService {

    private final CommonSrchMapper commonSrchMapper;

    public CommonSrchService(CommonSrchMapper commonSrchMapper) {
        this.commonSrchMapper = commonSrchMapper;
    }

    /**
     * 로케이션 검색 목록 조회
     */
    public List<CommonSrchVO> getLocationSearchList(CommonSrchVO vo) {
        // 필요 시 여기서 추가적인 비즈니스 로직(권한 체크 등)을 수행할 수 있습니다.
        return commonSrchMapper.selectLocationSearchList(vo);
    }
}