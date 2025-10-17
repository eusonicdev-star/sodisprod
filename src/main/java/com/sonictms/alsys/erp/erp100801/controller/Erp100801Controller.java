package com.sonictms.alsys.erp.erp100801.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sonictms.alsys.erp.erp100801.entity.Erp100801VO;
import com.sonictms.alsys.erp.erp100801.service.Erp100801Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp100801Controller {

    private final Erp100801Service erp100801Service;

    @Value("${common.java.kppApiSearch}")
    private String kppApiSearch;

    @Value("${common.java.kppApiUsername}")
    private String kppApiUsername;

    @Value("${common.java.kppApiUserpass}")
    private String kppApiUserpass;

    @Value("${common.java.kppApiStatus}")
    private String kppApiStatus;

    @GetMapping({"erp100801"})
    public ModelAndView getErp100801(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp100801/erp100801");
        return modelAndView;
    }

    @GetMapping({"erp1008010"})
    public ModelAndView getErp1008010(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp100801/erp1008010");
        return modelAndView;
    }

    @RequestMapping(value = {"kppSearch"}, method = {RequestMethod.POST})
    @ResponseBody
    public Erp100801VO kppSearch(@RequestBody Erp100801VO erp100801VO) {
        this.disableSslVerification();
        JSONObject data = new JSONObject();
        data.put("DLV_DT_FROM", erp100801VO.getDlvDtFrom());
        data.put("DLV_DT_TO", erp100801VO.getDlvDtTo());
        Erp100801VO vo = null;

        HttpURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader in = null;

        try {
            String host_url = this.kppApiSearch;
            log.info("kppApiSearch host_url : " + host_url);
            log.info("kppApiUsername        : " + this.kppApiUsername);
            log.info("kppApiUserpass        : " + this.kppApiUserpass);

            URL url = new URL(host_url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json;charset=UTF-8");
            String encodedAuth = "Basic " + Base64Util.encode(this.kppApiUsername + ":" + this.kppApiUserpass);
            conn.setRequestProperty("Authorization", encodedAuth);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(data.toString());
            bw.flush();
            bw.close();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String returnMsg = in.readLine();
            
            // 응답 메시지 null 체크
            if (returnMsg == null || returnMsg.trim().isEmpty()) {
                log.error("KPP API 응답이 비어있습니다.");
                vo = new Erp100801VO();
                vo.setResults(new ArrayList<>());
            } else {
                log.info("KPP API 응답 메시지: " + returnMsg);
                Gson gson = new Gson();
                try {
                    vo = gson.fromJson(returnMsg, (new TypeToken<Erp100801VO>() {
                    }).getType());
                } catch (Exception jsonException) {
                    log.error("JSON 파싱 중 오류 발생: " + jsonException.getMessage(), jsonException);
                    vo = new Erp100801VO();
                    vo.setResults(new ArrayList<>());
                }
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == 400) {
                log.info("     L> Erp100801VO KPP 응답코드 400 : 명령을 실행 오류");
            } else if (responseCode == 500) {
                log.info("     L> Erp100801VO KPP 응답코드 500 : 서버 에러.");
            } else {
                log.info("     L> Erp100801VO KPP 응답코드 " + responseCode);
            }
        } catch (java.net.ConnectException ce) {
            log.error("KPP API 서버 연결 실패: " + ce.getMessage(), ce);
            vo = new Erp100801VO();
            vo.setResults(new ArrayList<>());
        } catch (java.net.SocketTimeoutException ste) {
            log.error("KPP API 서버 연결 타임아웃: " + ste.getMessage(), ste);
            vo = new Erp100801VO();
            vo.setResults(new ArrayList<>());
        } catch (IOException ie) {
            log.error("KPP API 통신 중 IOException 발생: " + ie.getMessage(), ie);
            vo = new Erp100801VO();
            vo.setResults(new ArrayList<>());
        } catch (Exception ee) {
            log.error("KPP API 호출 중 예상치 못한 오류 발생: " + ee.getMessage(), ee);
            vo = new Erp100801VO();
            vo.setResults(new ArrayList<>());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.warn("BufferedReader close 중 오류", e);
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    log.warn("BufferedWriter close 중 오류", e);
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        if (vo == null) {
            log.error("KPP API 응답 파싱 실패: vo가 null입니다.");
            vo = new Erp100801VO();
            vo.setResults(new ArrayList<>());
        }
        
        if (vo.getResults() == null) {
            log.warn("KPP API 응답에서 results가 null입니다. 빈 리스트로 초기화합니다.");
            vo.setResults(new ArrayList<>());
        }
        
        log.info("kpp에서 불러온 데이터 목록 수 : " + vo.getResults().size());
        vo.setSaveUser(erp100801VO.getSaveUser());
        vo.setDlvDtFrom(erp100801VO.getDlvDtFrom());
        vo.setDlvDtTo(erp100801VO.getDlvDtTo());
        vo.setDtType(erp100801VO.getDtType());
        vo.setRefSoNoList(erp100801VO.getRefSoNoList());
        vo.setOutSoNoList(erp100801VO.getOutSoNoList());
        vo.setOutCmpyCd(erp100801VO.getOutCmpyCd());
        vo.setCmpyCd(erp100801VO.getCmpyCd());
        return this.erp100801Service.outCmpySave(vo);
    }

    @RequestMapping(value = {"erp100801OutCmpyList"}, method = {RequestMethod.POST})
    @ResponseBody
    public Erp100801VO erp100801OutCmpyList(@RequestBody Erp100801VO erp100801VO) {
        erp100801VO = this.erp100801Service.erp100801OutCmpyList(erp100801VO);
        return erp100801VO;
    }

    @RequestMapping(value = {"erp100801DataChk"}, method = {RequestMethod.POST})
    @ResponseBody
    public Erp100801VO erp100801DataChk(@RequestBody Erp100801VO erp100801VO) {
        erp100801VO = this.erp100801Service.erp100801DataChk(erp100801VO);
        return erp100801VO;
    }

    @RequestMapping(value = {"erp100801AlOrdrSave"}, method = {RequestMethod.POST})
    @ResponseBody
    public Erp100801VO erp100801AlOrdrSave(@RequestBody Erp100801VO erp100801VO) {
        this.erp100801Service.erp100801AlOrdrSave(erp100801VO);
        return erp100801VO;
    }

    public void disableSslVerification() {
        log.info("disableSslVerification() 수행 : ssl security Exception 방지");

        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> {
                // 호스트명 검증 로직
                if (hostname == null || hostname.trim().isEmpty()) {
                    log.warn("호스트명이 null이거나 비어있음");
                    return false;
                }

                // KPP API URL에서 호스트명 추출하여 검증
                try {
                    URL kppUrl = new URL(kppApiStatus);
                    String expectedHost = kppUrl.getHost();

                    if (hostname.equalsIgnoreCase(expectedHost)) {
                        log.info("호스트명 검증 성공: {}", hostname);
                        return true;
                    } else {
                        log.warn("호스트명 불일치 - 예상: {}, 실제: {}", expectedHost, hostname);
                        return false;
                    }
                } catch (Exception e) {
                    log.error("호스트명 검증 중 오류 발생: {}", e.getMessage());
                    return false;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("SSL 설정 중 오류 발생", e);
        }
    }
}