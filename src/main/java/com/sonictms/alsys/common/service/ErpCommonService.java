package com.sonictms.alsys.common.service;

import com.sonictms.alsys.common.entity.ErpCommonVO;
import com.sonictms.alsys.common.mapper.ErpCommonMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Slf4j
@RequiredArgsConstructor
@Service
public class ErpCommonService {

    private final ErpCommonMapper erpCommonMapper;

    // 20220517 정연호 추가 application에서 값 가져오기
    @Value("${common.java.kppApiStatus}")
    private String kppApiStatus;

    @Value("${common.java.kppApiUsername}")
    private String kppApiUsername;

    @Value("${common.java.kppApiUserpass}")
    private String kppApiUserpass;

    // 20220517 정연호 kpp 배송상태 변경을 위해 얼라이언스에서 보낼값을 조회하기. 1건 조회하기
    public ErpCommonVO erpCommoSendValueSearch(ErpCommonVO erpCommonVO) {
        erpCommonMapper.erpCommoSendValueSearch(erpCommonVO);
        return erpCommonVO;
    }

    // 20220517 정연호 모바일에서 kpp 배송상태 변경을 위해 얼라이언스에서 보낼값을 조회하기. 1건 조회하기
    public ErpCommonVO erpCommoSendValueSearchForMobile(ErpCommonVO erpCommonVO) {
        erpCommonMapper.erpCommoSendValueSearchForMobile(erpCommonVO);
        return erpCommonVO;
    }

    public ErpCommonVO kppDlvStateChange(ErpCommonVO erpCommonVO) {
        disableSslVerification();
        JSONArray jsonarr = new JSONArray();
        JSONObject data = new JSONObject();
        JSONObject req = new JSONObject();

        data.put("DLV_ORD_ID", erpCommonVO.getDlvOrdId());
        data.put("DLV_ORD_SEQ", erpCommonVO.getDlvOrdIdSeq());
        data.put("IMG_SND_YN", erpCommonVO.getImgSndYn());
        data.put("DLV_REQ_DT", erpCommonVO.getDlvReqDt());
        data.put("DLV_SET_DT", erpCommonVO.getDlvSetDt());
        data.put("SET_DRIVER_ID", erpCommonVO.getSetDriverId());
        data.put("SET_DRIVER_TL", erpCommonVO.getSetDriverTl());
        data.put("DLV_ORD_STAT", erpCommonVO.getDlvOrdStat());
        data.put("DLV_ORD_STAT_NM", erpCommonVO.getDlvOrdStatNm());
        data.put("ETC2", erpCommonVO.getEtc2());
        data.put("SERIAL_NO", erpCommonVO.getSerialNo());
        data.put("DLV_COMMENT", erpCommonVO.getDlvComment());
        data.put("MEMO", erpCommonVO.getMemo());
        data.put("LIFTING_WORK_YN", erpCommonVO.getLiftingWorkYn());
        data.put("HAPPY_CALL_YN", erpCommonVO.getHappyCallYn());
        data.put("HAPPY_CALL_MEMO", erpCommonVO.getHappyCallMemo());
        data.put("HAPPY_CALL_DT", erpCommonVO.getHappyCallDt());
        data.put("MOBILE_COM_DT", erpCommonVO.getMobileComDt());
        data.put("PRINT_RECEIPT_YN", erpCommonVO.getPrintReceiptYn());
        data.put("MOBILE_COM_ID", erpCommonVO.getMobileComId());

        jsonarr.add(data);

        req.put("request", jsonarr);

        ErpCommonVO vo = null;

        // JSON 데이터 HTTP POST 전송하기
        try {
            // String host_url =
            // "https://52.79.206.98:5521/restv2/WINUS.PAH:PAH_DLV_RR/PAH/DLV_RTN_INSERT";
            String host_url = kppApiStatus;

            log.info("kppApiStatus host_url : " + host_url);
            log.info("kppApiUsername        : " + kppApiUsername);
            log.info("kppApiUserpass        : " + kppApiUserpass);

            HttpURLConnection conn = null;

            URL url = new URL(host_url);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");// POST GET
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8"); // 타입설정(application/json) 형식으로 전송
            // (Request Body 전달시
            // application/json로 서버에 전달.)

            conn.setRequestProperty("Accept", "application/json;charset=UTF-8");
            // String encodedAuth = "Basic " + Base64Util.encode("eaitest" + ":" +
            // "eaitest");
            String encodedAuth = "Basic " + Base64Util.encode(kppApiUsername + ":" + kppApiUserpass);
            conn.setRequestProperty("Authorization", encodedAuth);

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            // POST방식으로 스트링을 통한 JSON 전송
            conn.setDoInput(true);
            conn.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

            bw.write(req.toString());
            // log.info("\n\n"+req.toString()+"\n");

            bw.flush();
            bw.close();
            // HTTP 응답 코드 수신
            int responseCode = conn.getResponseCode();
            if (responseCode == 400) {
                log.info("     L> ErpCommonVO KPP 응답코드 400 : 명령을 실행 오류");
            } else if (responseCode == 500) {
                log.info("     L> ErpCommonVO KPP 응답코드 500 : 서버 에러.");
            } else { // 정상 . 200 응답코드 . 기타 응답코드
                log.info("     L> ErpCommonVO KPP 응답코드 " + responseCode);
            }

            // 서버에서 보낸 응답 데이터 수신 받기
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String returnMsg = in.readLine();
            // log.info("returnMsg : " + returnMsg.toString());
            Gson gson = new Gson();

            // vo = gson.fromJson(returnMsg, new TypeToken<ErpCommonVO>() {}.getType());
            vo = gson.fromJson(returnMsg, ErpCommonVO.class);
            if (vo != null && vo.getResponse() != null) {
                log.info("vo.getResponse().getIfYn() : " + vo.getResponse().getIfYn());
                log.info("vo.getResponse().getIfCnt() : " + vo.getResponse().getIfCnt());
                log.info("vo.getResponse().getIfMessage() : " + vo.getResponse().getIfMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // log.info("kpp api 오더상태변경 : "+vo.toString());

        return vo;

    }

    // ssl security Exception 방지
    public void disableSslVerification() {
        // TODO Auto-generated method stub
        log.info("disableSslVerification() 수행 : ssl security Exception 방지");
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

};