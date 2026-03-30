package com.sonictms.alsys.common.util;

import com.sonictms.alsys.common.service.JusoApiService; // ★ 기존 서비스 Import 필수
import java.sql.*;

public class AptRoadAddrUpdater {

    // ==========================================
    // [설정 영역]
    // ==========================================
    private static final String DB_URL = "jdbc:sqlserver://sql16ssd-005.localnet.kr;databaseName=sonictmsdev_mssql;encrypt=false;trustServerCertificate=true;";
    private static final String DB_USER = "sonictmsdev_mssql";        // 본인 ID
    private static final String DB_PASS = "sonicdb*3415";  // 본인 PW

    // 도로명주소가 비어있는 데이터 조회
    private static final String SELECT_MISSING =
            "SELECT APT_CODE, ADDR_JIBUN, APT_NM " +
                    "  FROM TBL_APT_MASTER " +
                    " WHERE (ADDR_ROAD IS NULL OR ADDR_ROAD = '') " +
                    "   AND ADDR_JIBUN IS NOT NULL " +
                    "   AND ADDR_JIBUN <> ''";

    private static final String UPDATE_SQL =
            "UPDATE TBL_APT_MASTER SET ADDR_ROAD = ? WHERE APT_CODE = ?";

    // 기존 서비스 객체
    private JusoApiService jusoApiService;

    // 생성자에서 서비스 초기화
    public AptRoadAddrUpdater() {
        // [중요] JusoApiService를 수동으로 생성해야 합니다.
        // 만약 JusoApiService가 @Value로 키를 받거나 의존성이 있다면,
        // 해당 생성자에 직접 키를 넣어주거나 세팅해줘야 합니다.

        this.jusoApiService = new JusoApiService();

        // (참고) 만약 생성자에 API Key가 필요하다면?
        // this.jusoApiService = new JusoApiService("YOUR_API_KEY");
    }

    public static void main(String[] args) {
        new AptRoadAddrUpdater().startUpdate();
    }

    public void startUpdate() {
        Connection conn = null;
        PreparedStatement selectStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;

        try {
            System.out.println(">>> DB 연결 중...");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            conn.setAutoCommit(false);

            selectStmt = conn.prepareStatement(SELECT_MISSING);
            rs = selectStmt.executeQuery();

            updateStmt = conn.prepareStatement(UPDATE_SQL);

            int successCount = 0;
            int totalCount = 0;
            int failCount = 0;

            System.out.println(">>> 도로명 주소 보정 작업 시작 (with JusoApiService)...");

            while (rs.next()) {
                totalCount++;
                String aptCode = rs.getString("APT_CODE");
                String jibunAddr = rs.getString("ADDR_JIBUN"); // "광명동 123 광명아파트"
                String aptNm = rs.getString("APT_NM");         // "광명"

                // 1. 검색어 최적화 (지번 주소에서 아파트 이름 제거)
                // API는 '지번 + 아파트명'이 섞여 있으면 검색을 잘 못할 때가 있음
                String query = jibunAddr;
                if (aptNm != null && !aptNm.isEmpty()) {
                    query = query.replace(aptNm, "").trim();
                }
                if (query.length() < 2) query = jibunAddr; // 너무 짧아지면 원복

                // 2. ★ 기존 서비스 호출
                String roadAddr = null;
                try {
                    // JusoApiService의 메서드명이 searchRoadAddress라고 가정
                    roadAddr = jusoApiService.searchRoadAddress(query);
                } catch (Exception e) {
                    System.err.println("API 호출 중 에러: " + e.getMessage());
                }

                // 3. 결과 처리
                if (roadAddr != null && !roadAddr.isEmpty()) {
                    updateStmt.setString(1, roadAddr);
                    updateStmt.setString(2, aptCode);
                    updateStmt.addBatch();
                    successCount++;
                    System.out.println("[성공] " + query + " -> " + roadAddr);
                } else {
                    failCount++;
                    System.out.println("[실패] 결과 없음: " + query);
                }

                // 4. 배치 실행 (50건 단위)
                if (successCount > 0 && successCount % 50 == 0) {
                    updateStmt.executeBatch();
                    conn.commit();
                    System.out.println(">>> " + successCount + "건 업데이트 완료...");

                    // API 보호 딜레이 (기존 서비스 내부에 딜레이가 없다면 추가)
                    try { Thread.sleep(200); } catch (InterruptedException ie) {}
                }
            }

            // 남은 데이터 처리
            updateStmt.executeBatch();
            conn.commit();

            System.out.println("==========================================");
            System.out.println(">>> 작업 완료!");
            System.out.println(">>> 총 대상: " + totalCount);
            System.out.println(">>> 성공: " + successCount);
            System.out.println(">>> 실패: " + failCount);
            System.out.println("==========================================");

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (selectStmt != null) selectStmt.close(); } catch (Exception e) {}
            try { if (updateStmt != null) updateStmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}