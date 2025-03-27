package com.sonictms.alsys.commCode.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sonictms.alsys.commCode.entity.CommCodeVO;
import com.sonictms.alsys.commCode.entity.SendAlrmTalkVO;
import com.sonictms.alsys.commCode.mapper.CommCodeMapper;

@Service
public class CommCodeService  {

	@Autowired
	private CommCodeMapper commCodeMapper;


	public List<CommCodeVO> comComboList(CommCodeVO commCodeVO) {
		
		return commCodeMapper.comComboList(commCodeVO);
	}

	public List<CommCodeVO> commSrchList(CommCodeVO commCodeVO) {
			
			return commCodeMapper.commSrchList(commCodeVO);
		}


	//알람톡 발송 결과를 성공일때만 저장
	public SendAlrmTalkVO saveAlrmTmpResult(SendAlrmTalkVO sendAlrmTalkVO) {
		
		return commCodeMapper.saveAlrmTmpResult(sendAlrmTalkVO);
	}
	
	
	//알람톡 발송 결과를 스케줄 대량 발송의 결과값 저장
	public SendAlrmTalkVO sendScdlAlrmResult(SendAlrmTalkVO sendAlrmTalkVO) {
		
		return commCodeMapper.sendScdlAlrmResult(sendAlrmTalkVO);
	}


} 