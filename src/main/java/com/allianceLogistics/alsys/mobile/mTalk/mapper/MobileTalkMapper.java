package com.allianceLogistics.alsys.mobile.mTalk.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.allianceLogistics.alsys.mobile.mTalk.entity.MobileTalkVO;



@Component
@Mapper
public interface MobileTalkMapper {

	//모바일 알림톡 불러오기 - 알림톡 보내기 전에 불러오기
	MobileTalkVO mSrchTalk(MobileTalkVO mobileTalkVO);

	//모바일 알림톡 발송결과를 DB에 저장
	MobileTalkVO mSaveTalk(MobileTalkVO mobileTalkVO);
	
	
	//모바일 해피콜완료 4000 => 5000 으로 바꾸기
	MobileTalkVO mSaveStat(MobileTalkVO mobileTalkVO);

}