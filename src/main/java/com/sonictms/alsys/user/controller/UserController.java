package com.sonictms.alsys.user.controller;

import com.sonictms.alsys.user.entity.Repw;
import com.sonictms.alsys.user.entity.User;
import com.sonictms.alsys.user.service.UserService;
import com.sonictms.alsys.config.ProfileConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Controller
public class UserController {

  private final UserService userService;
  private final ProfileConfig profileConfig;

  @GetMapping(value = {"/"})
  public ModelAndView getIndex() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("user/layout");
    modelAndView.addObject("activeProfile", profileConfig.getProfileDisplayName());
    return modelAndView;
  }

  @GetMapping(value = {"login"})
  public ModelAndView getLoginPage() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("user/login");
    return modelAndView;
  }

  @GetMapping(value = {"loginRe"})
  public ModelAndView getLoginPageRe() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("user/loginRe");
    return modelAndView;
  }

  // 인증문자 페이지
  @GetMapping(value = {"repw"})
  public ModelAndView getRepwPage() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("user/repw");
    return modelAndView;
  }

  //20220517 정연호 추가 application에서 값 가져오기
  @Value("${common.java.bizMsgUrl}")
  private String bizMsgUrl;

  @Value("${common.java.bizMsgUserid}")
  private String bizMsgUserid;

  // 비밀번호 변경을 위한 인증번호 요청하기
  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"repwReqSmsByPhoneNo"}, method = RequestMethod.POST)
  @ResponseBody
  public Repw repwReqSmsByPhoneNo(Repw param) {
    userService.repwReqSmsByPhoneNo(param);

    JSONArray jsonarr = getJsonArray(param);

    log.info("모바일 인증번호 알림톡 발송하기 : " + jsonarr.toString());

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
      log.info("모바일 인증번호 알림톡 발송하기 응답메시지 : " + returnMsg);

      // HTTP 응답 코드 수신
      int responseCode = conn.getResponseCode();
      if (responseCode == 400) {
        log.info("모바일 인증번호 알림톡 발송하기 응답코드 400 : 명령을 실행 오류");
      } else if (responseCode == 500) {
        log.info("모바일 인증번호 알림톡 발송하기 응답코드 500 : 서버 에러.");
      } else { // 정상 . 200 응답코드 . 기타 응답코드
        log.info("모바일 인증번호 알림톡 발송하기 응답코드 " + responseCode);
      }

    } catch (IOException ie) {
      log.info("IOException " + ie.getCause());
      ie.printStackTrace();
    } catch (Exception ee) {
      log.info("Exception " + ee.getCause());
      ee.printStackTrace();
    }

    param.setReqNo("");
    return param;
  }

  private JSONArray getJsonArray(Repw param) {
    JSONArray jsonArr = new JSONArray();

    JSONObject data = new JSONObject();

    data.put("message_type", "at");
    data.put("phn", param.getPhoneNo());
    data.put("profile", "a9edf1ad8d55c0b88b089bc48404cfbfb7912037");
    data.put("reserveDt", "00000000000000");
    data.put("tmplId", "ALLIANCE_CNFM_NO");
    data.put("msg", "	얼라이언스 인증번호 안내\r\n" + param.getReqNo() + "\r\n" + "타인에게 노출하지 마세요.");
    data.put("smsKind", "S");
    data.put("msgSms", "얼라이언스 인증번호 안내\r\n" + param.getReqNo() + "\r\n" + "타인에게 노출하지 마세요.");
    data.put("smsSender", "0315269848");      //20230401 정연호. 0315269846 을 0315269848 로 변경
    data.put("smsLmsTit", "인증번호");
    data.put("smsOnly", "N");

    jsonArr.add(data);
    return jsonArr;
  }

  // 인증번호를 넣고 인증하기
  @RequestMapping(value = {"repwSendReqNo"}, method = RequestMethod.POST)
  @ResponseBody
  public Repw repwSendReqNo(Repw param) {
    userService.repwSendReqNo(param);
    return param;
  }

  // 비밀번호변경하는 페이지 접속
  @PostMapping(value = {"changePw"})
  public ModelAndView getChangePw(@Valid Repw repw) {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("user/changePw");
    modelAndView.addObject("repw", repw);
    return modelAndView;
  }

  // 비밀번호변경하는 작업 수행
  @RequestMapping(value = {"changePwSend"}, method = RequestMethod.POST)
  @ResponseBody
  public Repw changePwSend(Repw param) {
    userService.changePwSend(param);
    return param;
  }

  // 모바일비밀번호변경하는 작업 수행
  @RequestMapping(value = {"mobile/mChangePwSend"}, method = RequestMethod.POST)
  @ResponseBody
  public Repw mChangePwSend(@RequestBody Repw param) {
    userService.changePwSend(param);
    return param;
  }

  @GetMapping(value = {"layout"})
  public ModelAndView getLayout(ModelAndView modelAndView) {
    modelAndView.setViewName("user/layout");
    modelAndView.addObject("activeProfile", profileConfig.getProfileDisplayName());
    return modelAndView;
  }

  // 세션타임 연장
  @RequestMapping(value = "sessionTimePlus")
  @ResponseBody
  public void sessionTimePlus() throws Exception {
    return;
  }

  @GetMapping("registration")
  public ModelAndView getRegistrationPage() {
    ModelAndView modelAndView = new ModelAndView();
    User user = new User();
    modelAndView.addObject("user", user);
    modelAndView.setViewName("user/registration");
    return modelAndView;
  }

  @PostMapping("registration")
  public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
    ModelAndView modelAndView = new ModelAndView();

    // 아이디로 정보를 찾아서 중복여부체크
    User userExists = userService.findUserInfoByLoginId(user);
    if (userExists != null) {
      bindingResult.rejectValue("loginId", "error.loginId",
              "There is already a user registered with the loginId provided");
    }
    if (bindingResult.hasErrors()) {
      modelAndView.setViewName("user/registration");
    } else {
      userService.saveUser(user);
      modelAndView.addObject("successMessage", "User has been registered successfully");
      modelAndView.addObject("user", new User());
      modelAndView.setViewName("user/registration");
    }
    return modelAndView;
  }

  @GetMapping("exception")
  public ModelAndView getUserPermissionExceptionPage() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("user/access-denied");
    return mv;
  }

  @GetMapping("/myInfo")
  public ModelAndView getUserPage() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("user/myInfo");
    return mv;
  }

  @PostMapping("/updatePassword")
  public ResponseEntity<Boolean> updatePassword(
          HttpSession session,
          @RequestParam String currentPassword,
          @RequestParam String newPassword
  ) {
    boolean success = userService.updatePassword((String) session.getAttribute("userId"), currentPassword, newPassword);
    return ResponseEntity.ok(success);
  }
}