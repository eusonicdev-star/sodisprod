package com.sonictms.alsys.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZipcodeFileLoader {

    // [MS-SQL 설정]
    private static final String DB_URL = "jdbc:sqlserver://sql16ssd-005.localnet.kr;databaseName=sonictmsdev_mssql;encrypt=false;trustServerCertificate=true;";
    private static final String DB_USER = "sonictmsdev_mssql";        // 본인 ID
    private static final String DB_PASS = "sonicdb*3415";  // 본인 PW

    // 파일 설정 (탭으로 구분된 txt 파일)
    private static final String FILE_DIR = "C:\\addr";
    private static final String FILE_ENCODING = "UTF-8"; // 또는 EUC-KR 확인 필요

    // 타겟 테이블 (동 정보는 없으므로 아파트명과 주소 매핑 위주)
    private static final String INSERT_SQL =
            "INSERT INTO TBL_KOREA_BUILDING (FULL_ROAD_ADDR, APT_NM, SEARCH_KEY, SIDO, SIGUNGU, ROAD_NM) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final int BATCH_SIZE = 3000;

    public static void main(String[] args) {
        new ZipcodeFileLoader().startLoading();
    }

    public void startLoading() {
        Connection conn = null;
        PreparedStatement pstmt = null;

        // 정규식: "주소 (괄호내용)" 패턴 분리
        // 그룹 1: 주소 (서울특별시... 658)
        // 그룹 2: 괄호 내용 (우이동, 성원아파트)
        Pattern pattern = Pattern.compile("^(.*?)\\s*\\((.*)\\)$");

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(INSERT_SQL);

            File dir = new File(FILE_DIR);
            File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

            if (files == null) return;

            long count = 0;
            System.out.println(">>> 적재 시작...");

            for (File file : files) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), FILE_ENCODING));
                String line;

                while ((line = br.readLine()) != null) {
                    // 1. 탭(\t)으로 분리
                    // 0: 우편번호, 1: 전체주소, 2: 법정동명
                    String[] parts = line.split("\t");
                    if (parts.length < 2) continue;

                    String fullString = parts[1]; // "서울특별시 강북구 삼양로 658(우이동, 성원아파트)"

                    // 2. 정규식 파싱
                    Matcher matcher = pattern.matcher(fullString);
                    if (matcher.find()) {
                        String realAddr = matcher.group(1).trim(); // "서울특별시 강북구 삼양로 658"
                        String parenContent = matcher.group(2).trim(); // "우이동, 성원아파트" or "우이동"

                        // 3. 아파트 이름 추출 로직
                        // 쉼표(,)로 나눴을 때 마지막 부분이 보통 아파트 이름임
                        // 예: "우이동, 성원아파트" -> ["우이동", "성원아파트"]
                        String[] parenParts = parenContent.split(",");
                        String aptNm = "";

                        // 괄호 안에 내용이 있고, "아파트"나 "빌라", "오피스텔" 등으로 끝나거나
                        // 쉼표가 있어서 뒤에 명칭이 따로 있는 경우 가져옴
                        if (parenParts.length > 1) {
                            aptNm = parenParts[parenParts.length - 1].trim();
                        } else {
                            // 쉼표가 없는데 "아파트" 글자가 포함된 경우 (예: "(성원아파트)")
                            if (parenContent.contains("아파트") || parenContent.contains("빌라") || parenContent.contains("오피스텔")) {
                                aptNm = parenContent;
                            }
                        }

                        // 아파트 이름이 없으면 저장 안 함 (선택사항)
                        if (aptNm.isEmpty()) continue;

                        // 4. 추가 정보 파싱 (시도, 시군구 등 - 주소를 공백으로 잘라서 대충 채움)
                        String[] addrTokens = realAddr.split(" ");
                        String sido = (addrTokens.length > 0) ? addrTokens[0] : "";
                        String sigungu = (addrTokens.length > 1) ? addrTokens[1] : "";
                        String roadNm = ""; // 정밀하게 하려면 더 복잡하지만 일단 패스

                        // 5. 검색키 (아파트 이름만)
                        // 동 정보가 없으므로 아파트 이름 자체가 검색키가 됨
                        String searchKey = aptNm;

                        pstmt.setString(1, realAddr);
                        pstmt.setString(2, aptNm);
                        pstmt.setString(3, searchKey);
                        pstmt.setString(4, sido);
                        pstmt.setString(5, sigungu);
                        pstmt.setString(6, roadNm);

                        pstmt.addBatch();
                        count++;
                    }

                    if (count % BATCH_SIZE == 0) {
                        pstmt.executeBatch();
                        conn.commit();
                        System.out.println(">>> " + count + "건 처리...");
                    }
                }
                br.close();
            }
            pstmt.executeBatch();
            conn.commit();
            System.out.println(">>> 최종 완료: " + count + "건");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}