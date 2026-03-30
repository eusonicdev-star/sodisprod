package com.sonictms.alsys.common.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

@Slf4j
@Service
public class JusoApiService {

    private static final String API_KEY = "devU01TX0FVVEgyMDI2MDEyNzA4NDUxNDExNzUwMDc="; // 본인 키 확인
    private static final String API_URL = "https://business.juso.go.kr/addrlink/addrLinkApi.do";

    // [추가됨] SSL 인증서 무시 설정
    static {
        disableSslVerification();
    }

    private static void disableSslVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            log.error("SSL 설정 무시 중 오류", e);
        }
    }

    public String searchRoadAddress(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return null;

        try {
            StringBuilder urlBuilder = new StringBuilder(API_URL);
            urlBuilder.append("?confmKey=").append(API_KEY);
            urlBuilder.append("&currentPage=1");
            urlBuilder.append("&countPerPage=1");
            urlBuilder.append("&keyword=").append(URLEncoder.encode(keyword, "UTF-8"));
            urlBuilder.append("&resultType=json");

            URL url = new URL(urlBuilder.toString());
            // HttpURLConnection 대신 HttpsURLConnection 사용 가능 (위의 static 설정 덕분에 통과됨)
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            // 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(sb.toString());
            JsonNode common = root.path("results").path("common");

            if (!"0".equals(common.path("errorCode").asText())) {
                log.warn("Juso API Error Message: {}", common.path("errorMessage").asText());
                return null;
            }

            // 결과 개수 확인
            int totalCount = Integer.parseInt(common.path("totalCount").asText());
            if (totalCount > 0) {
                String cleanAddress = root.path("results").path("juso").get(0).path("roadAddrPart1").asText();
                System.out.println(keyword+">>>>"+cleanAddress);
                log.info("Juso API 변환 성공: [{}] -> [{}]", keyword, cleanAddress);
                return cleanAddress;
            } else {
                log.info("Juso API 결과 없음(0건): [{}]", keyword);
                return null; // 0건이면 null 반환 -> Service에서 2차 정규화로 넘어감
            }

        } catch (Exception e) {
            // SSL 에러 등 발생 시 로그 찍고 null 반환 -> Service에서 2차 정규화로 넘어감
            log.error("Juso API 호출 실패 (Fallback 진행 예정): {} / Error: {}", keyword, e.getMessage());
            return null;
        }
    }
}