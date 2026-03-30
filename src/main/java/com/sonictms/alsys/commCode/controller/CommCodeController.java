package com.sonictms.alsys.commCode.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sonictms.alsys.commCode.entity.CommCodeVO;
import com.sonictms.alsys.commCode.entity.SendAlrmTalkVO;
import com.sonictms.alsys.commCode.service.CommCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@Transactional // 트랜잭션 오류시 롤백
public class CommCodeController {

	public static final String AlimTalkUserId = "eugenesonic";
	private final CommCodeService commCodeService;

	// 알림톡 API 설정값
	@Value("${common.java.bizMsgUrl}")
	private String bizMsgUrl;

	@Value("${common.java.bizMsgUserid}")
	private String bizMsgUserid;

    @PostMapping("comComboList")
	@ResponseBody
    public List<CommCodeVO> comComboList(CommCodeVO commCodeVO) {
        return commCodeService.comComboList(commCodeVO);
	}

	@RequestMapping(value = {"mobile/comComboList"}, method = RequestMethod.POST)
	@ResponseBody
	public List<CommCodeVO> comComboListForMobile(@RequestBody CommCodeVO CommCodeVO) {
		return commCodeService.comComboList(CommCodeVO);
	}

	@PostMapping(value = {"commSrch"})
	public ModelAndView commSrch(ModelAndView modelAndView, @Valid CommCodeVO CommCodeVO) {
		modelAndView.setViewName("commCode/commSrch");
		modelAndView.addObject("sendObject", CommCodeVO);
		return modelAndView;
	}

	@PostMapping(value = {"prodSrch"})
	public ModelAndView prodSrch(ModelAndView modelAndView, @Valid CommCodeVO CommCodeVO) {
		modelAndView.setViewName("commCode/prodSrch");
		modelAndView.addObject("sendObject", CommCodeVO);
		return modelAndView;
	}

	@PostMapping(value = {"prodSrchMto"})
	public ModelAndView prodSrchMto(ModelAndView modelAndView, @Valid CommCodeVO CommCodeVO) {
		modelAndView.setViewName("commCode/prodSrchMto");
		modelAndView.addObject("sendObject", CommCodeVO);
		return modelAndView;
	}

	@RequestMapping(value = {"commSrchList"}, method = RequestMethod.POST)
	@ResponseBody
	public List<CommCodeVO> commSrchList(CommCodeVO CommCodeVO) {
		return commCodeService.commSrchList(CommCodeVO);
	}

	@RequestMapping(value = {"commSrchListMto"}, method = RequestMethod.POST)
	@ResponseBody
	public List<CommCodeVO> commSrchListMto(CommCodeVO CommCodeVO) {
		return commCodeService.commSrchListMto(CommCodeVO);
	}

    /**
     * 알림톡 발송 관련 메서드
     *
     * @param sendAlrmTalkVO
     * @return
     */
	@RequestMapping(value = {"sendAlrmTalk"}, method = RequestMethod.POST)
	@ResponseBody
	public List<SendAlrmTalkVO> sendAlrmTalk(@RequestBody SendAlrmTalkVO sendAlrmTalkVO) {
		JSONArray jsonArray = createAlrmTalkJsonArray(sendAlrmTalkVO);
		return sendAlrmTalkToApi(jsonArray);
	}

	@RequestMapping(value = {"saveAlrmTmpResult"}, method = RequestMethod.POST)
	@ResponseBody
	public SendAlrmTalkVO saveAlrmTmpResult(SendAlrmTalkVO sendAlrmTalkVO) {
		return commCodeService.saveAlrmTmpResult(sendAlrmTalkVO);
	}

	@RequestMapping(value = {"sendScdlAlrm"}, method = RequestMethod.POST)
	@ResponseBody
	public List<SendAlrmTalkVO> sendScdlAlrm(@RequestBody List<SendAlrmTalkVO> sendAlrmTalkVOList) {
		List<SendAlrmTalkVO> resultList = new ArrayList<>();
		
		for (SendAlrmTalkVO alrmTalkVO : sendAlrmTalkVOList) {
			JSONArray jsonArray = createAlrmTalkJsonArray(alrmTalkVO);

			List<SendAlrmTalkVO> apiResponse = sendAlrmTalkToApi(jsonArray);
			if (!apiResponse.isEmpty()) {
				SendAlrmTalkVO resultVO = processScheduledAlrmTalkResult(apiResponse.get(0), alrmTalkVO);
				resultList.add(resultVO);
			}
		}
		
		return resultList;
	}

	// ==================== 공통 유틸리티 메서드 ====================

	/**
	 * 알림톡 발송을 위한 JSON 배열 생성
	 */
	private JSONArray createAlrmTalkJsonArray(SendAlrmTalkVO alrmTalkVO) {
		JSONArray jsonArray = new JSONArray();
		JSONObject data = new JSONObject();

		data.put("message_type", alrmTalkVO.getMessage_type());
		data.put("phn", alrmTalkVO.getPhn());
		data.put("profile", alrmTalkVO.getProfile());
		data.put("reserveDt", alrmTalkVO.getTalkReserveDt());
		data.put("tmplId", alrmTalkVO.getTmplId());
		data.put("msg", alrmTalkVO.getMsg());
		data.put("smsKind", alrmTalkVO.getSmsKind());
		data.put("msgSms", alrmTalkVO.getMsgSms());
		data.put("smsSender", alrmTalkVO.getSmsSender());
		data.put("smsLmsTit", alrmTalkVO.getSmsLmsTit());
		data.put("smsOnly", alrmTalkVO.getSmsOnly());

		if (alrmTalkVO.getButton1() != null) {
			data.put("button1", alrmTalkVO.getButton1());
		}
		if (alrmTalkVO.getButton2() != null) {
			data.put("button2", alrmTalkVO.getButton2());
		}

		jsonArray.add(data);
		return jsonArray;
	}

	/**
	 * 알림톡 API 호출 공통 메서드
	 */
	private List<SendAlrmTalkVO> sendAlrmTalkToApi(JSONArray jsonArray) {
		HttpURLConnection conn = null;
		BufferedWriter bw = null;
		BufferedReader in = null;
		
		try {
			conn = createHttpConnection();
			
			// JSON 데이터 전송
			bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			bw.write(jsonArray.toString());
			bw.flush();

			// 응답 데이터 수신
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String returnMsg = in.readLine();
			
			log.info("알림톡 API 응답메시지 : {}", returnMsg);
			
			// 응답 코드 로깅
			logResponseCode(conn.getResponseCode());
			
			// JSON 응답을 객체로 변환
			return parseApiResponse(returnMsg);
			
		} catch (IOException ie) {
			log.error("알림톡 API 호출 중 IOException 발생: {}", ie.getMessage(), ie);
		} catch (Exception ee) {
			log.error("알림톡 API 호출 중 Exception 발생: {}", ee.getMessage(), ee);
		} finally {
			closeResources(bw, in, conn);
		}
		
		return new ArrayList<>();
	}

	/**
	 * HTTP 연결 생성
	 */
	private HttpURLConnection createHttpConnection() throws IOException {
		URL url = new URL(bizMsgUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		conn.setRequestProperty("userid", bizMsgUserid);
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);
		conn.setDoOutput(true);

		log.info("bizMsgUrl : {}", bizMsgUrl);
		log.info("bizMsgUserid : {}", bizMsgUserid);

		return conn;
	}

	/**
	 * 응답 코드 로깅
	 */
	private void logResponseCode(int responseCode) {
		switch (responseCode) {
			case 200:
				log.info("알림톡 API 응답코드 {} : 정상 처리", responseCode);
				break;
			case 400:
				log.info("알림톡 API 응답코드 {} : 명령 실행 오류", responseCode);
				break;
			case 500:
				log.info("알림톡 API 응답코드 {} : 서버 에러", responseCode);
				break;
			default:
				log.info("알림톡 API 응답코드 {} : 기타 응답", responseCode);
		}
	}

	/**
	 * API 응답을 객체로 파싱
	 */
	private List<SendAlrmTalkVO> parseApiResponse(String returnMsg) {
		Gson gson = new Gson();
		return gson.fromJson(returnMsg, new TypeToken<ArrayList<SendAlrmTalkVO>>() {}.getType());
	}

	/**
	 * 스케줄 알림톡 결과 처리
	 */
	private SendAlrmTalkVO processScheduledAlrmTalkResult(SendAlrmTalkVO apiResponse, SendAlrmTalkVO originalVO) {
		// API 응답 데이터를 원본 VO에 매핑
		apiResponse.setTblSoMId(originalVO.getTblSoMId());
		apiResponse.setUserid(AlimTalkUserId);
		apiResponse.setCode(apiResponse.getCode());
		apiResponse.setMessage_type(apiResponse.getData().getType());
		apiResponse.setPhn(apiResponse.getData().getPhn());
		apiResponse.setProfile(originalVO.getProfile());
		apiResponse.setTalkReserveDt(originalVO.getTalkReserveDt());
		apiResponse.setMsgSms(originalVO.getMsgSms());
		apiResponse.setSmsLmsTit(originalVO.getSmsLmsTit());
		apiResponse.setTmplId(originalVO.getTmplId());
		apiResponse.setCmplMsgCd(apiResponse.getMessage());
		apiResponse.setSaveUser(originalVO.getSaveUser());

		// 발송 결과 저장
		try {
			SendAlrmTalkVO savedResult = commCodeService.sendScdlAlrmResult(apiResponse);
			apiResponse.setRtnYn(savedResult.getRtnYn());
			apiResponse.setRtnMsg(savedResult.getRtnMsg());
		} catch (Exception e) {
			log.error("알림톡 발송 결과 저장 실패: {}", e.getMessage(), e);
			apiResponse.setRtnYn("N");
			apiResponse.setRtnMsg("알림톡 발송 결과 저장 실패");
		}

		return apiResponse;
	}

	/**
	 * 리소스 정리
	 */
	private void closeResources(BufferedWriter bw, BufferedReader in, HttpURLConnection conn) {
		try {
			if (bw != null) bw.close();
			if (in != null) in.close();
			if (conn != null) conn.disconnect();
		} catch (IOException e) {
			log.warn("리소스 정리 중 IOException 발생: {}", e.getMessage());
		}
	}
}