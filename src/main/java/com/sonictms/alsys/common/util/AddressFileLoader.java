package com.sonictms.alsys.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddressFileLoader {

    // ==========================================
    // [MS-SQL 설정 영역] 본인 환경에 맞게 수정하세요
    // ==========================================
    // 1. DB 접속 URL (localhost, 1433포트, DB명: addr_backup)
    // encrypt=false: 로컬 개발 시 SSL 인증서 에러 방지용
    private static final String DB_URL = "jdbc:sqlserver://sql16ssd-005.localnet.kr;databaseName=sonictmsdev_mssql;encrypt=false;trustServerCertificate=true;";
    private static final String DB_USER = "sonictmsdev_mssql";        // 본인 ID
    private static final String DB_PASS = "sonicdb*3415";  // 본인 PW


    // 파일 설정
    private static final String FILE_DIR = "C:\\addr";
    private static final String FILE_ENCODING = "EUC-KR"; // 한글 깨짐 방지

    // ★ 테이블 및 컬럼 설정 (테이블명: addr_backup, 컬럼명: addr)
    private static final String INSERT_SQL = "INSERT INTO addr_backup (addr) VALUES (?)";

    private static final int BATCH_SIZE = 5000; // 5000건씩 커밋

    public static void main(String[] args) {
        // 드라이버 로딩
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("MS-SQL 드라이버가 없습니다.");
            return;
        }

        startLoading();
    }

    public static void startLoading() {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            System.out.println(">>> DB 연결 시도...");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            conn.setAutoCommit(false); // 수동 커밋 (속도 향상 필수)
            System.out.println(">>> DB 연결 성공!");

            pstmt = conn.prepareStatement(INSERT_SQL);

            File dir = new File(FILE_DIR);
            File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".txt"));

            if (files == null || files.length == 0) {
                System.out.println(">>> C:\\addr 폴더에 .txt 파일이 없습니다.");
                return;
            }

            System.out.println(">>> 총 " + files.length + "개의 파일 발견. 적재 시작...");

            long totalCount = 0;
            long startTime = System.currentTimeMillis();

            for (File file : files) {
                System.out.println(">>> 처리 중: " + file.getName());
                int fileCount = processFile(file, pstmt);
                conn.commit(); // 파일 하나 끝날 때마다 커밋
                totalCount += fileCount;
                System.out.println("    완료 (건수: " + fileCount + ")");
            }

            conn.commit(); // 최종 커밋

            long endTime = System.currentTimeMillis();
            System.out.println("==================================================");
            System.out.println(">>> 작업 완료!");
            System.out.println(">>> 총 적재 건수: " + totalCount);
            System.out.println(">>> 소요 시간: " + (endTime - startTime) / 1000 + "초");
            System.out.println("==================================================");

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        } finally {
            closeResource(conn, pstmt);
        }
    }

    private static int processFile(File file, PreparedStatement pstmt) {
        int count = 0;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), FILE_ENCODING));
            String line;

            while ((line = br.readLine()) != null) {
                // 빈 줄은 건너뛰기 (선택사항)
                if (line.trim().isEmpty()) continue;

                // ★ 라인 통째로 넣기
                pstmt.setString(1, line);
                pstmt.addBatch();
                count++;

                // 배치 실행
                if (count % BATCH_SIZE == 0) {
                    pstmt.executeBatch();
                    pstmt.clearBatch();
                }
            }

            // 남은 것 처리
            pstmt.executeBatch();
            pstmt.clearBatch();

        } catch (Exception e) {
            System.err.println("파일 읽기 에러: " + file.getName());
            e.printStackTrace();
        } finally {
            try { if (br != null) br.close(); } catch (Exception e) {}
        }
        return count;
    }

    private static void closeResource(Connection conn, PreparedStatement pstmt) {
        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}