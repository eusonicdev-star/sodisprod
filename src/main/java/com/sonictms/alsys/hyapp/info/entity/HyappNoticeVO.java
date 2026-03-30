package com.sonictms.alsys.hyapp.info.entity;

import lombok.Data;

@Data
public class HyappNoticeVO {
    private Long noticeId;      // 알림 고유 ID
    private String cmpyCd;      // 회사 코드
    private String noticeType;  // 알림 유형 (예: REPLENISH)
    private String title;       // 알림 제목
    private String content;     // 알림 상세 내용
    private String confirmYn;   // 확인 여부 (Y/N)
    private String saveTime;    // 발생 시간
    private String userId;      // 대상 사용자 ID
    private String saveUser;    // 알림 발생시킨 사람 (SAVE_USER)
    private String confirmUser; // 알림 확인한 사람 (CONFIRM_USER)
}