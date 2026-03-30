package com.sonictms.alsys.common.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GoogleMapService {

    // 구글 클라우드 콘솔에서 발급받은 API Key
    private static final String GOOGLE_API_KEY = "AIzaSyDQt7pDu7gGnhtS9jjxYFIxl3bY5-tY_pY";

    public Map<String, Object> getCoordinate(String address) {
        if (address == null || address.trim().isEmpty()) return null;

        HttpURLConnection conn = null;
        BufferedReader br = null;

        try {
            // 1. URL 설정 (보내주신 코드 로직)
            String encodedAddr = URLEncoder.encode(address, "UTF-8");
            String urlStr = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddr
                    + "&key=" + GOOGLE_API_KEY
                    + "&language=ko";

            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();

            // 2. 헤더 및 연결 설정 (보내주신 코드 반영)
            conn.setRequestMethod("GET");
            conn.setRequestProperty("content-type", "application/json");
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            // conn.setDoOutput(true); // GET 요청에서는 body를 안 보내므로 true로 하면 에러가 날 수 있어 주석 처리했습니다.

            int responseCode = conn.getResponseCode();
            log.info(">>> Google API ResponseCode: {}", responseCode);

            // 3. 응답 읽기
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
                log.warn("Google API Error Stream 읽기 진입");
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String jsonResult = sb.toString();
            // log.debug("Google API JSON: {}", jsonResult); // 필요 시 주석 해제

            // 4. JSON 파싱 (Spring Boot 기본 Jackson 사용)
            // 보내주신 json-simple 로직과 동일하게 동작합니다.
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResult);
            String status = root.path("status").asText();

            if ("OK".equals(status)) {
                // results 배열의 첫 번째 결과 가져오기
                JsonNode firstResult = root.path("results").get(0);
                JsonNode location = firstResult.path("geometry").path("location");

                String lat = location.path("lat").asText(); // 위도
                String lng = location.path("lng").asText(); // 경도

                log.info(">>> Google API 성공: {} -> lat:{}, lng:{}", address, lat, lng);

                // 5. 결과 리턴 (카카오 로직과 호환되도록 x, y로 매핑)
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("x", lng); // 경도 (longitude)
                resultMap.put("y", lat); // 위도 (latitude)
                resultMap.put("lat", lat);
                resultMap.put("lng", lng);

                return resultMap;
            } else {
                log.warn(">>> Google API 실패 (Status: {}): {}", status, address);
                return null;
            }

        } catch (Exception e) {
            log.error("Google API 호출 중 에러 발생: {}", e.getMessage());
            return null;
        } finally {
            // 리소스 정리
            try { if (br != null) br.close(); } catch (Exception ex) {}
            if (conn != null) conn.disconnect();
        }
    }
}