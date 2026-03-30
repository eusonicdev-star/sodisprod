package com.sonictms.alsys.hyapp.info.controller;

import com.sonictms.alsys.hyapp.info.entity.HyappNoticeVO;
import com.sonictms.alsys.hyapp.info.service.HyappNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/hyapp/api/notice")
@RequiredArgsConstructor
public class HyappNoticeController {
    private final HyappNoticeService noticeService;

    @GetMapping("/list")
    public List<HyappNoticeVO> list(@RequestParam String cmpyCd) {
        return noticeService.getNoticeList(cmpyCd);
    }

    @GetMapping("/unread-count")
    public int unreadCount(@RequestParam String cmpyCd) {
        return noticeService.getUnreadCount(cmpyCd);
    }

    @PostMapping("/confirm")
    public void confirm(@RequestParam Long noticeId) {
        noticeService.confirmNotice(noticeId);
    }
}