package com.sonictms.alsys.common.service; // 공통 패키지로 추천 (또는 현재 패키지에 포함)

import com.sonictms.alsys.common.mapper.AddressCoordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AddressCoordService {

    @Autowired
    private AddressCoordMapper addressCoordMapper;

    /**
     * 정제된 검색 키(searchKey)로 저장된 좌표가 있는지 조회
     * @param searchKey 정규화된 주소 문자열
     * @return 좌표 정보 Map (lat, lng 등) / 없으면 null
     */
    public Map<String, Object> selectCoordByKey(String searchKey) {
        return addressCoordMapper.selectCoordByKey(searchKey);
    }

    /**
     * 외부 API(구글 등)에서 찾은 좌표 정보를 DB에 저장 (캐싱)
     * @param paramMap (searchKey, rawAddr, lat, lng, aptNm, dongNm 포함)
     */
    @Transactional
    public void insertCoord(Map<String, Object> paramMap) {
        // 중복 방지를 위해 MERGE 구문이 XML에 있더라도,
        // 서비스 단에서도 한번 더 체크하거나 예외처리를 할 수 있습니다.
        try {
            addressCoordMapper.insertCoord(paramMap);
        } catch (Exception e) {
            // 동시에 여러 요청이 와서 PK 충돌 등이 나더라도 프로세스는 멈추지 않도록 로그만 남김
            System.err.println("좌표 캐시 저장 중 오류 (무시가능): " + e.getMessage());
        }
    }
}