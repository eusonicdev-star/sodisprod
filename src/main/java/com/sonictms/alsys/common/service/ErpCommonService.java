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
    private final Gson gson = new Gson();

    @Value("${common.java.kppApiStatus}")
    private String kppApiStatus;

    @Value("${common.java.kppApiUsername}")
    private String kppApiUsername;

    @Value("${common.java.kppApiUserpass}")
    private String kppApiUserpass;

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String ACCEPT = "application/json;charset=UTF-8";

    /**
     * KPP 배송상태 변경을 위해 얼라이언스에서 보낼값을 조회 (1건)
     */
    public ErpCommonVO erpCommoSendValueSearch(ErpCommonVO erpCommonVO) {
        erpCommonMapper.erpCommoSendValueSearch(erpCommonVO);
        return erpCommonVO;
    }

    /**
     * 모바일에서 KPP 배송상태 변경을 위해 얼라이언스에서 보낼값을 조회 (1건)
     */
    public ErpCommonVO erpCommoSendValueSearchForMobile(ErpCommonVO erpCommonVO) {
        erpCommonMapper.erpCommoSendValueSearchForMobile(erpCommonVO);
        return erpCommonVO;
    }

    /**
     * KPP 배송상태 변경 API 호출
     */
    public ErpCommonVO kppDlvStateChange(ErpCommonVO erpCommonVO) {
        disableSslVerification();

        JSONObject requestBody = createKppRequest(erpCommonVO);
        return sendKppApiRequest(requestBody);
    }

    /**
     * KPP API 요청 데이터 생성
     */
    private JSONObject createKppRequest(ErpCommonVO erpCommonVO) {
        JSONArray jsonArray = new JSONArray();
        JSONObject data = new JSONObject();

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

        jsonArray.add(data);

        JSONObject request = new JSONObject();
        request.put("request", jsonArray);
        return request;
    }

    /**
     * KPP API 요청 전송
     */
    private ErpCommonVO sendKppApiRequest(JSONObject requestBody) {
        HttpURLConnection connection = null;

        try {
            connection = createHttpConnection();
            sendRequest(connection, requestBody);

            int responseCode = connection.getResponseCode();
            logResponseCode(responseCode);

            String responseMessage = readResponse(connection);
            return parseResponse(responseMessage);

        } catch (Exception e) {
            log.error("KPP API 요청 중 오류 발생", e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * HTTP 연결 생성
     */
    private HttpURLConnection createHttpConnection() throws Exception {
        URL url = new URL(kppApiStatus);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", CONTENT_TYPE);
        connection.setRequestProperty("Accept", ACCEPT);

        String encodedAuth = "Basic " + Base64Util.encode(kppApiUsername + ":" + kppApiUserpass);
        connection.setRequestProperty("Authorization", encodedAuth);

        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        logApiInfo();

        return connection;
    }

    /**
     * API 정보 로깅
     */
    private void logApiInfo() {
        log.info("kppApiStatus host_url : {}", kppApiStatus);
        log.info("kppApiUsername        : {}", kppApiUsername);
        log.info("kppApiUserpass        : {}", kppApiUserpass);
    }

    /**
     * 요청 데이터 전송
     */
    private void sendRequest(HttpURLConnection connection, JSONObject requestBody) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
            writer.write(requestBody.toString());
            writer.flush();
        }
    }

    /**
     * 응답 코드 로깅
     */
    private void logResponseCode(int responseCode) {
        switch (responseCode) {
            case 400:
                log.info("L> ErpCommonVO KPP 응답코드 400 : 명령을 실행 오류");
                break;
            case 500:
                log.info("L> ErpCommonVO KPP 응답코드 500 : 서버 에러");
                break;
            default:
                log.info("L> ErpCommonVO KPP 응답코드 {}", responseCode);
                break;
        }
    }

    /**
     * 응답 데이터 읽기
     */
    private String readResponse(HttpURLConnection connection) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.readLine();
        }
    }

    /**
     * 응답 데이터 파싱
     */
    private ErpCommonVO parseResponse(String responseMessage) {
        if (responseMessage == null) {
            return null;
        }

        ErpCommonVO vo = gson.fromJson(responseMessage, ErpCommonVO.class);

        if (vo != null && vo.getResponse() != null) {
            log.info("vo.getResponse().getIfYn() : {}", vo.getResponse().getIfYn());
            log.info("vo.getResponse().getIfCnt() : {}", vo.getResponse().getIfCnt());
            log.info("vo.getResponse().getIfMessage() : {}", vo.getResponse().getIfMessage());
        }

        return vo;
    }

    /**
     * SSL 보안 검증 비활성화
     */
    private void disableSslVerification() {
        log.info("disableSslVerification() 수행 : ssl security Exception 방지");

        try {
            TrustManager[] trustAllCerts = createTrustAllManager();
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> {
                if (hostname == null || hostname.trim().isEmpty()) {
                    return false;
                }
                
                try {
                    URL kppUrl = new URL(kppApiStatus);
                    String expectedHost = kppUrl.getHost();
                    
                    if (expectedHost != null && hostname.equalsIgnoreCase(expectedHost)) {
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

    /**
     * 모든 인증서를 신뢰하는 TrustManager 생성
     */
    private TrustManager[] createTrustAllManager() {
        return new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // 모든 클라이언트 인증서 신뢰
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // 모든 서버 인증서 신뢰
            }
        }};
    }
}