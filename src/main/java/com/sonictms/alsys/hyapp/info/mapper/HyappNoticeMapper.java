package com.sonictms.alsys.hyapp.info.mapper;

import com.sonictms.alsys.hyapp.info.entity.HyappNoticeVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface HyappNoticeMapper {
    // 미확인 알림 우선순위로 전체 리스트 조회
    List<HyappNoticeVO> selectNoticeList(String cmpyCd);

    // 읽지 않은 알림 개수 조회
    int selectUnreadCount(String cmpyCd);

    // 알림 저장 처리
    int insertNotice(HyappNoticeVO vo);

    // 알림 확인 처리
    int updateNoticeConfirm(Long noticeId);
}