package com.allianceLogistics.alsys.commCode.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.allianceLogistics.alsys.commCode.entity.CommCodeVO;
import com.allianceLogistics.alsys.commCode.entity.SendAlrmTalkVO;
import com.allianceLogistics.alsys.commCode.service.CommCodeService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Transactional // 트랜잭션 오류시 롤백
public class CommCodeController {
	@Autowired
	CommCodeService commCodeService;

	@GetMapping(value = { "robots.txt" })
	public ModelAndView getLoginPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("robots.txt");
		return modelAndView;
	}

	// 시스템
	@RequestMapping(value = { "comComboList" }, method = RequestMethod.POST)
	@ResponseBody
	public List<CommCodeVO> comComboList(CommCodeVO CommCodeVO) {
		List<CommCodeVO> list = commCodeService.comComboList(CommCodeVO);
		return list;
	}

	// 모바일
	@RequestMapping(value = { "mobile/comComboList" }, method = RequestMethod.POST)
	@ResponseBody
	public List<CommCodeVO> comComboListForMobile(@RequestBody CommCodeVO CommCodeVO) {
		List<CommCodeVO> list = commCodeService.comComboList(CommCodeVO);
		return list;
	}

	// 공통코드 찾기 팝업
	@PostMapping(value = { "commSrch" })
	public ModelAndView commSrch(ModelAndView modelAndView, @Valid CommCodeVO CommCodeVO) {
		modelAndView.setViewName("commCode/commSrch");
		modelAndView.addObject("sendObject", CommCodeVO);
		return modelAndView;
	}

	// 품목코드 찾기 팝업
	@PostMapping(value = { "prodSrch" })
	public ModelAndView prodSrch(ModelAndView modelAndView, @Valid CommCodeVO CommCodeVO) {
		modelAndView.setViewName("commCode/prodSrch");
		modelAndView.addObject("sendObject", CommCodeVO);
		return modelAndView;
	}

	// 공통코드 찾기 팝업의 그리그 리스트 조회하기
	@RequestMapping(value = { "commSrchList" }, method = RequestMethod.POST)
	@ResponseBody
	public List<CommCodeVO> commSrchList(CommCodeVO CommCodeVO) {
		List<CommCodeVO> list = commCodeService.commSrchList(CommCodeVO);
		return list;
	}

	//20220517 정연호 추가 application에서 값 가져오기
	@Value("${common.java.bizMsgUrl}")
	private String bizMsgUrl;
	
	@Value("${common.java.bizMsgUserid}")
	private String bizMsgUserid;
		
	// 알림톡 보내기
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "sendAlrmTalk" }, method = RequestMethod.POST)
	@ResponseBody
	public List<SendAlrmTalkVO> sendAlrmTalk(@RequestBody SendAlrmTalkVO sendAlrmTalkVO) {
		JSONArray jsonarr = new JSONArray();

		JSONObject data = new JSONObject();

		data.put("message_type", sendAlrmTalkVO.getMessage_type());
		data.put("phn", sendAlrmTalkVO.getPhn());
		data.put("profile", sendAlrmTalkVO.getProfile());
		data.put("reserveDt", sendAlrmTalkVO.getTalkReserveDt());
		data.put("tmplId", sendAlrmTalkVO.getTmplId());
		data.put("msg", sendAlrmTalkVO.getMsg());
		data.put("smsKind", sendAlrmTalkVO.getSmsKind());
		data.put("msgSms", sendAlrmTalkVO.getMsgSms());
		data.put("smsSender", sendAlrmTalkVO.getSmsSender());
		data.put("smsLmsTit", sendAlrmTalkVO.getSmsLmsTit());
		data.put("smsOnly", sendAlrmTalkVO.getSmsOnly());
		if (sendAlrmTalkVO.getButton1() != null) {
			data.put("button1", sendAlrmTalkVO.getButton1());
		}

		if (sendAlrmTalkVO.getButton2() != null) {
			data.put("button2", sendAlrmTalkVO.getButton2());
		}

		jsonarr.add(data);

		List<SendAlrmTalkVO> vo = null;

		log.info("sendAlrmTalk 알림톡 보내기 :" + jsonarr.toString());
		// JSON 데이터 HTTP POST 전송하기

		try {
			//String host_url = "https://alimtalk-api.bizmsg.kr/v2/sender/send";
			String host_url = bizMsgUrl;
			HttpURLConnection conn = null;

			URL url = new URL(host_url);
			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");// POST GET
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8"); // 타입설정(application/json) 형식으로 전송
																						// (Request Body 전달시
																						// application/json로 서버에 전달.)
			//conn.setRequestProperty("userid", "alliance");
			conn.setRequestProperty("userid", bizMsgUserid);
			
			log.info("bizMsgUrl : " + bizMsgUrl);
			log.info("bizMsgUserid : " + bizMsgUserid);
			
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);

			// POST방식으로 스트링을 통한 JSON 전송
			conn.setDoOutput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

			bw.write(jsonarr.toString());

			bw.flush();
			bw.close();

			// 서버에서 보낸 응답 데이터 수신 받기
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String returnMsg = in.readLine();

			log.info("sendAlrmTalk 알림톡 보내기 응답메시지 : " + returnMsg);

			Gson gson = new Gson();

			vo = gson.fromJson(returnMsg, new TypeToken<ArrayList<SendAlrmTalkVO>>() {
			}.getType());

			// HTTP 응답 코드 수신
			int responseCode = conn.getResponseCode();
			if (responseCode == 400) {
				log.info("     L> sendAlrmTalk 알림톡 보내기 응답코드 400 : 명령을 실행 오류");
			} else if (responseCode == 500) {
				log.info("     L> sendAlrmTalk 알림톡 보내기 응답코드 500 : 서버 에러.");
			} else { // 정상 . 200 응답코드 . 기타 응답코드
				log.info("     L> sendAlrmTalk 알림톡 보내기 응답코드 "+ responseCode);
			}
		} catch (IOException ie) {
			log.info("IOException " + ie.getCause());
			ie.printStackTrace();
		} catch (Exception ee) {
			log.info("Exception " + ee.getCause());
			ee.printStackTrace();
		}
		return vo;
	}

	// 알람톡 발송 결과를 성공일때만 저장
	@RequestMapping(value = { "saveAlrmTmpResult" }, method = RequestMethod.POST)
	@ResponseBody
	public SendAlrmTalkVO saveAlrmTmpResult(SendAlrmTalkVO sendAlrmTalkVO) {
		sendAlrmTalkVO = commCodeService.saveAlrmTmpResult(sendAlrmTalkVO);
		return sendAlrmTalkVO;
	}

	// 알림톡 스케줄 발송 보내기
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "sendScdlAlrm" }, method = RequestMethod.POST)
	@ResponseBody
	public List<SendAlrmTalkVO> sendScdlAlrm(@RequestBody List<SendAlrmTalkVO> sendAlrmTalkVO) {
		List<SendAlrmTalkVO> vo = null;
		List<SendAlrmTalkVO> rerutnValue = new ArrayList<SendAlrmTalkVO>();
		JSONObject data = new JSONObject();

		for (int i = 0; i < sendAlrmTalkVO.size(); i++) {
			JSONArray jsonarr = new JSONArray();

			data = new JSONObject();

			data.put("message_type", sendAlrmTalkVO.get(i).getMessage_type());
			data.put("phn", sendAlrmTalkVO.get(i).getPhn());
			data.put("profile", sendAlrmTalkVO.get(i).getProfile());
			data.put("reserveDt", sendAlrmTalkVO.get(i).getTalkReserveDt());
			data.put("tmplId", sendAlrmTalkVO.get(i).getTmplId());
			data.put("msg", sendAlrmTalkVO.get(i).getMsg());
			data.put("smsKind", sendAlrmTalkVO.get(i).getSmsKind());
			data.put("msgSms", sendAlrmTalkVO.get(i).getMsgSms());
			data.put("smsSender", sendAlrmTalkVO.get(i).getSmsSender());
			data.put("smsLmsTit", sendAlrmTalkVO.get(i).getSmsLmsTit());
			data.put("smsOnly", sendAlrmTalkVO.get(i).getSmsOnly());

			if (sendAlrmTalkVO.get(i).getButton1() != null) {
				data.put("button1", sendAlrmTalkVO.get(i).getButton1());
			}

			if (sendAlrmTalkVO.get(i).getButton2() != null) {
				data.put("button2", sendAlrmTalkVO.get(i).getButton2());
			}

			jsonarr.add(data);
			log.info("sendScdlAlrm 알림톡 스케줄 : " + jsonarr.toString());
			// JSON 데이터 HTTP POST 전송하기

			try {
				//String host_url = "https://alimtalk-api.bizmsg.kr/v2/sender/send"; 
				String host_url = bizMsgUrl;
				HttpURLConnection conn = null;

				URL url = new URL(host_url);
				conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");// POST GET
				conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8"); // 타입설정(application/json)
																							// 형식으로 전송 (Request Body 전달시
																							// application/json로 서버에
																							// 전달.)
				//conn.setRequestProperty("userid", "alliance"); // 0505
				conn.setRequestProperty("userid", bizMsgUserid);
				
				log.info("bizMsgUrl : " + bizMsgUrl);
				log.info("bizMsgUserid : " + bizMsgUserid);
				
				conn.setConnectTimeout(10000);
				conn.setReadTimeout(10000);

				// POST방식으로 스트링을 통한 JSON 전송
				conn.setDoOutput(true);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

				bw.write(jsonarr.toString());

				bw.flush();
				bw.close();

				// 서버에서 보낸 응답 데이터 수신 받기
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String returnMsg = in.readLine();

				log.info("sendScdlAlrm 알림톡 스케줄 응답메시지 : " + returnMsg);
				Gson gson = new Gson();

				vo = gson.fromJson(returnMsg, new TypeToken<ArrayList<SendAlrmTalkVO>>() {
				}.getType());

				// HTTP 응답 코드 수신
				int responseCode = conn.getResponseCode();
				if (responseCode == 400) {
					log.info("     L> sendScdlAlrm 알림톡 스케줄 응답코드 400 : 명령을 실행 오류");
				} else if (responseCode == 500) {
					log.info("     L> sendScdlAlrm 알림톡 스케줄 응답코드 500 : 서버 에러.");
				} else { // 정상 . 200 응답코드 . 기타 응답코드
					log.info("     L> sendScdlAlrm 알림톡 스케줄 응답코드 "+ responseCode);
				}
			} catch (IOException ie) {
				log.info("IOException " + ie.getCause());
				ie.printStackTrace();
			} catch (Exception ee) {
				log.info("Exception " + ee.getCause());
				ee.printStackTrace();
			}

			vo.get(0).setTblSoMId(sendAlrmTalkVO.get(i).getTblSoMId());
			vo.get(0).setUserid("alliance");
			vo.get(0).setCode(vo.get(0).getCode());
			vo.get(0).setMessage_type(vo.get(0).getData().getType());
			vo.get(0).setPhn(vo.get(0).getData().getPhn());
			vo.get(0).setProfile(sendAlrmTalkVO.get(i).getProfile());
			vo.get(0).setTalkReserveDt(sendAlrmTalkVO.get(i).getTalkReserveDt());
			vo.get(0).setMsgSms(sendAlrmTalkVO.get(i).getMsgSms());
			vo.get(0).setSmsLmsTit(sendAlrmTalkVO.get(i).getSmsLmsTit());

			vo.get(0).setTmplId(sendAlrmTalkVO.get(i).getTmplId());

			vo.get(0).setCmplMsgCd(vo.get(0).getMessage());

			vo.get(0).setSaveUser(sendAlrmTalkVO.get(i).getSaveUser());

			SendAlrmTalkVO rtnVO = null;
			try {
				rtnVO = commCodeService.sendScdlAlrmResult(vo.get(0));
			} catch (Exception e) {
				rtnVO.setRtnYn("N");
				rtnVO.setRtnMsg("알림톡 발송 결과 저장 실패");
			}

			vo.get(0).setRtnYn(rtnVO.getRtnYn());
			vo.get(0).setRtnMsg(rtnVO.getRtnMsg());
			rerutnValue.add(vo.get(0));
		}
		return rerutnValue;
	}
}