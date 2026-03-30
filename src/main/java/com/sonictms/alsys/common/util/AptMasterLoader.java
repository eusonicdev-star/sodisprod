package com.sonictms.alsys.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AptMasterLoader {

    // [MS-SQL 설정]
    private static final String DB_URL = "jdbc:sqlserver://sql16ssd-005.localnet.kr;databaseName=sonictmsdev_mssql;encrypt=false;trustServerCertificate=true;";
    private static final String DB_USER = "sonictmsdev_mssql";        // 본인 ID
    private static final String DB_PASS = "sonicdb*3415";  // 본인 PW

    private static final String FILE_PATH = "C:\\addr\\20260123_단지_기본정보.csv";
    private static final String FILE_ENCODING = "UTF-8";

    // [수정] 도로명(ADDR_ROAD)과 지번(ADDR_JIBUN) 둘 다 저장
    private static final String INSERT_SQL =
            "INSERT INTO TBL_APT_MASTER (APT_CODE, APT_NM, ADDR_ROAD, ADDR_JIBUN, DONG_CNT, DA_CNT) VALUES (?, ?, ?, ?, ?, ?)";

    public static void main(String[] args) {
        new AptMasterLoader().startLoading();
    }

    public void startLoading() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        BufferedReader br = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(INSERT_SQL);

            File file = new File(FILE_PATH);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), FILE_ENCODING));

            String line;
            int lineCount = 0;
            int successCount = 0;

            System.out.println(">>> 적재 시작: " + file.getName());

            while ((line = br.readLine()) != null) {
                lineCount++;
                if (lineCount <= 2 || line.startsWith("시도") || line.contains("OPENAPI")) continue;

                try {
                    String[] data = parseCsvLine(line);
                    if (data.length < 6) continue;

                    // 1. 원본 데이터 추출
                    String aptCode = getSafeData(data, 4);   // 단지코드
                    String originAptNm = getSafeData(data, 5); // 원본 단지명 ("낙원아파트")
                    String rawJibun = getSafeData(data, 7);  // 원본 지번주소
                    String rawRoad = getSafeData(data, 9);   // 원본 도로명주소

                    // 2. 아파트명 정제 ("아파트" 삭제)
                    // 예: "낙원아파트" -> "낙원", "광명 아파트" -> "광명"
                    String cleanAptNm = originAptNm.replace("아파트", "").trim();

                    // 3. 주소 정제 (끝에 붙은 '원본 단지명' 제거)
                    // 주의: 주소 뒤에는 '아파트' 글자가 붙어있는 원본 이름이 붙어있을 확률이 높음
                    String cleanRoad = removeSuffix(rawRoad, originAptNm);
                    String cleanJibun = removeSuffix(rawJibun, originAptNm);

                    // 4. 숫자 파싱
                    int dongCnt = parseInt(getSafeData(data, 13));
                    int daCnt = (int) parseFloat(getSafeData(data, 14));

                    if (aptCode.isEmpty() || cleanAptNm.isEmpty()) continue;

                    // 5. DB 입력
                    pstmt.setString(1, aptCode);
                    pstmt.setString(2, cleanAptNm);  // 정제된 이름 ("낙원")
                    pstmt.setString(3, cleanRoad);   // 정제된 도로명
                    pstmt.setString(4, cleanJibun);  // 정제된 지번
                    pstmt.setInt(5, dongCnt);
                    pstmt.setInt(6, daCnt);

                    pstmt.addBatch();
                    successCount++;

                    if (successCount % 3000 == 0) {
                        pstmt.executeBatch();
                        conn.commit();
                        System.out.println(">>> " + successCount + "건 저장 중...");
                    }

                } catch (Exception e) {
                    System.err.println("Line " + lineCount + " 에러: " + e.getMessage());
                }
            }

            pstmt.executeBatch();
            conn.commit();
            System.out.println(">>> 최종 완료! 총 " + successCount + "건.");

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        } finally {
            try { if (br != null) br.close(); } catch (Exception ex) {}
            try { if (pstmt != null) pstmt.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }

    // [Helper] 주소 끝에 단지명이 있으면 제거
    private String removeSuffix(String addr, String suffix) {
        if (addr == null || addr.isEmpty()) return "";
        String cleanAddr = addr.trim();
        String suffixTrimmed = suffix.trim();

        // 주소가 단지명으로 끝나면 제거
        if (cleanAddr.endsWith(suffixTrimmed)) {
            return cleanAddr.substring(0, cleanAddr.length() - suffixTrimmed.length()).trim();
        }
        return cleanAddr;
    }

    private String[] parseCsvLine(String line) {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '\"') inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) { list.add(sb.toString().trim()); sb.setLength(0); }
            else sb.append(c);
        }
        list.add(sb.toString().trim());
        return list.toArray(new String[0]);
    }

    private String getSafeData(String[] data, int index) {
        if (index >= data.length) return "";
        String val = data[index];
        if (val.startsWith("\"") && val.endsWith("\"")) return val.substring(1, val.length() - 1);
        return val;
    }

    private int parseInt(String str) {
        try { return Integer.parseInt(str.replaceAll("[^0-9]", "")); } catch (Exception e) { return 0; }
    }

    private float parseFloat(String str) {
        try { return Float.parseFloat(str); } catch (Exception e) { return 0f; }
    }
}