package com.sonictms.alsys.common.util;

import java.sql.*;

public class AddressSplitter {

    // [MS-SQL 설정] 본인 환경에 맞게
    private static final String DB_URL = "jdbc:sqlserver://sql16ssd-005.localnet.kr;databaseName=sonictmsdev_mssql;encrypt=false;trustServerCertificate=true;";
    private static final String DB_USER = "sonictmsdev_mssql";        // 본인 ID
    private static final String DB_PASS = "sonicdb*3415";  // 본인 PW

        // 원본 읽기
        private static final String SELECT_SQL = "SELECT addr FROM addr_backup";

        // 타겟 입력
        private static final String INSERT_SQL =
                "INSERT INTO TBL_KOREA_BUILDING " +
                        "(FULL_ROAD_ADDR, APT_NM, DONG_NM, SEARCH_KEY, SIDO, SIGUNGU, ROAD_NM, BUILD_MAIN_NO, BUILD_SUB_NO) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        private static final int BATCH_SIZE = 3000;

        public static void main(String[] args) {
            new AddressSplitter().startMigration();
        }

        public void startMigration() {
            Connection conn = null;
            PreparedStatement selectStmt = null;
            PreparedStatement insertStmt = null;
            ResultSet rs = null;

            try {
                System.out.println(">>> DB 연결...");
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                conn.setAutoCommit(false);

                selectStmt = conn.prepareStatement(SELECT_SQL);
                selectStmt.setFetchSize(1000);
                rs = selectStmt.executeQuery();

                insertStmt = conn.prepareStatement(INSERT_SQL);

                long count = 0;
                System.out.println(">>> 마이그레이션 시작 (참고항목 포함)...");

                while (rs.next()) {
                    String rawLine = rs.getString(1);
                    if (rawLine == null) continue;
                    String[] data = rawLine.split("\\|", -1);

                    // 데이터 길이 체크 (26번 컬럼까지 보려면 길이가 충분해야 함)
                    if (data.length < 26) continue;

                    // 1. 기본 주소 파싱
                    String sido = data[1];
                    String sigungu = data[2];
                    String roadNm = data[9];
                    String mainNoStr = data[11];
                    String subNoStr = data[12];

                    String col13_AptNm = data[13]; // 시군구건물명 (공식 명칭)
                    String col14_DongNm = data[14]; // 상세건물명 (동)
                    String col25_RefNm = data[25]; // ★ 참고항목 (여기에 아파트명이 숨어있음!)

                    // 2. [핵심] 아파트 이름 결정 로직
                    // 공식 명칭이 있으면 쓰고, 없으면 참고항목(26번째)을 쓴다.
                    String finalAptNm = "";
                    if (!col13_AptNm.trim().isEmpty()) {
                        finalAptNm = col13_AptNm.trim();
                    } else if (!col25_RefNm.trim().isEmpty()) {
                        finalAptNm = col25_RefNm.trim();
                    }

                    // 3. 필터링: 아파트 이름조차 없으면 진짜 버림 (주소만 있는 건 의미 없음)
                    if (finalAptNm.isEmpty()) {
                        continue;
                    }

                    // 동 정보 정리 (없으면 빈 문자열)
                    String finalDongNm = col14_DongNm.trim();

                    // 4. 전체 도로명 주소 조립
                    int mainNo = parseInt(mainNoStr);
                    int subNo = parseInt(subNoStr);

                    StringBuilder roadAddrInfo = new StringBuilder();
                    roadAddrInfo.append(sido).append(" ").append(sigungu).append(" ").append(roadNm).append(" ").append(mainNo);
                    if (subNo > 0) roadAddrInfo.append("-").append(subNo);

                    String fullRoadAddr = roadAddrInfo.toString().trim();

                    // 5. 검색 키 조립
                    // 동이 있으면 "아파트명 + 동", 없으면 "아파트명"만 (이게 중요!)
                    String searchKey = finalAptNm;
                    if (!finalDongNm.isEmpty()) {
                        searchKey += " " + finalDongNm;
                    }

                    // 6. DB 입력
                    insertStmt.setString(1, fullRoadAddr);
                    insertStmt.setString(2, finalAptNm);   // 찾아낸 아파트명
                    insertStmt.setString(3, finalDongNm);  // 동 (없을 수도 있음)
                    insertStmt.setString(4, searchKey);
                    insertStmt.setString(5, sido);
                    insertStmt.setString(6, sigungu);
                    insertStmt.setString(7, roadNm);
                    insertStmt.setInt(8, mainNo);
                    insertStmt.setInt(9, subNo);

                    insertStmt.addBatch();
                    count++;

                    if (count % BATCH_SIZE == 0) {
                        insertStmt.executeBatch();
                        insertStmt.clearBatch();
                        conn.commit();
                        System.out.println(">>> " + count + "건 처리 중...");
                    }
                }

                insertStmt.executeBatch();
                conn.commit();
                System.out.println(">>> 최종 완료! 총 " + count + "건 저장됨.");

            } catch (Exception e) {
                e.printStackTrace();
                try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            } finally {
                try { if (rs != null) rs.close(); } catch (Exception e) {}
                try { if (selectStmt != null) selectStmt.close(); } catch (Exception e) {}
                try { if (insertStmt != null) insertStmt.close(); } catch (Exception e) {}
                try { if (conn != null) conn.close(); } catch (Exception e) {}
            }
        }

        private int parseInt(String str) {
            try { return Integer.parseInt(str); } catch (Exception e) { return 0; }
        }
    }