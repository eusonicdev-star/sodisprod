package com.allianceLogistics.alsys.commCode.mapper;


import com.allianceLogistics.alsys.commCode.entity.CommCodeVO;
import com.allianceLogistics.alsys.commCode.entity.SendAlrmTalkVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;



@Mapper
public interface CommCodeMapper {

	List<CommCodeVO> comComboList(CommCodeVO commCodeVO);
	
	List<CommCodeVO> commSrchList(CommCodeVO commCodeVO);
	
	
	
	//알람톡 발송 결과를 성공일때만 저장
	SendAlrmTalkVO saveAlrmTmpResult(SendAlrmTalkVO sendAlrmTalkVO);
	
	
	//알람톡 발송 결과를 스케줄 대량 발송의 결과값 저장
	SendAlrmTalkVO sendScdlAlrmResult(SendAlrmTalkVO sendAlrmTalkVO);
	
}