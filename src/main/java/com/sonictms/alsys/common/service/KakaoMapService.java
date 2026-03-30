package com.sonictms.alsys.common.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KakaoMapService {

    // [중요] 발급받은 REST API 키
    //private final String KAKAO_API_KEY = "2f57137c2c221b4e905484d9208bef8c";
    private final String KAKAO_API_KEY = "bafcef628cefd6001bbfc63417b631d6";

    /**
     * 카카오 로컬 API (키워드 검색) 호출 - SSL 인증서 무시 버전
     */
    public Map<String, Object> getKakaoCoordinate(String query) {
        HttpURLConnection conn = null;
        BufferedReader br = null;

        try {
            // 1. URL 설정
            String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query="
                    + URLEncoder.encode(query, "UTF-8");

            URL url = new URL(apiUrl);

            // [핵심 수정] HTTPS 연결일 경우 SSL 검증 무시 설정 적용
            if (url.getProtocol().toLowerCase().equals("https")) {
                disableSslVerification();
            }

            conn = (HttpURLConnection) url.openConnection();

            // 2. 헤더 설정
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK " + KAKAO_API_KEY);
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);

            // 3. 응답 코드 확인
            int responseCode = conn.getResponseCode();
            System.out.println("Kakao API Response Code: {}"+ responseCode);

            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else { // 에러 발생
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
                System.out.println("Kakao API Error: {}"+ responseCode);
                // 에러 내용을 읽어서 로그에 남김
                StringBuilder sbErr = new StringBuilder();
                String lineErr;
                while ((lineErr = br.readLine()) != null) {
                    sbErr.append(lineErr);
                }
                System.out.println("Error Body: {}"+ sbErr.toString());
                return null;
            }

            // 4. 응답 데이터 읽기
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String jsonResponse = sb.toString();
            System.out.println("Kakao API jsonResponse"+ jsonResponse);
            // 5. 파싱 (json-simple 사용)
            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                JSONParser parser = new JSONParser();
                JSONObject jsonObj = (JSONObject) parser.parse(jsonResponse);
                JSONArray documents = (JSONArray) jsonObj.get("documents");

                // 검색 결과가 1개 이상 있을 경우
                if (documents != null && documents.size() > 0) {
                    JSONObject doc = (JSONObject) documents.get(0);

                    Map<String, Object> map = new HashMap<>();
                    // [주의] 카카오는 x가 경도(lng), y가 위도(lat)
                    map.put("lat", Double.parseDouble(doc.get("y").toString()));
                    map.put("lng", Double.parseDouble(doc.get("x").toString()));
                    map.put("apiType", "KAKAO");

                    return map;
                }
            }

        } catch (Exception e) {
            System.out.println("카카오 API 호출 실패 [Query: {}] : {}"+ query+ e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
                if (conn != null) conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * [신규 추가] SSL 인증서 검증을 무시하는 설정 (Global)
     * - PKIX path building failed 에러 해결용
     */
    private void disableSslVerification() {
        try {
            // 모든 인증서를 신뢰하는 TrustManager 생성
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                    }
            };

            // SSLContext에 TrustManager 등록
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            // 전역 SSL 소켓 팩토리 교체 (HttpsURLConnection이 이 설정을 따름)
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // 모든 호스트네임 허용 (IP와 도메인이 달라도 통과)
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) { return true; }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}