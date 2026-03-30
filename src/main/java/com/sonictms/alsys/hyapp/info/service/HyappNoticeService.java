package com.sonictms.alsys.hyapp.info.service;

import com.sonictms.alsys.hyapp.info.entity.HyappNoticeVO;
import com.sonictms.alsys.hyapp.info.mapper.HyappNoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HyappNoticeService {
    private final HyappNoticeMapper noticeMapper;

    public List<HyappNoticeVO> getNoticeList(String cmpyCd) {
        return noticeMapper.selectNoticeList(cmpyCd);
    }

    public int getUnreadCount(String cmpyCd) {
        return noticeMapper.selectUnreadCount(cmpyCd);
    }

    @Transactional
    public void confirmNotice(Long noticeId) {
        noticeMapper.updateNoticeConfirm(noticeId);
    }
}