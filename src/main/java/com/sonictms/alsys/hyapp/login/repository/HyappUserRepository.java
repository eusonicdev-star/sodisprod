package com.sonictms.alsys.hyapp.login.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HyappUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public HyappUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // USER_ID로 USER_PW(암호화된 비밀번호) 가져오기
    public Optional<String> findPasswordByUserId(String userId) {
        String sql = "SELECT USER_PW FROM TBL_USER_M WHERE USER_ID = ?";

        List<String> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("USER_PW"),
                userId
        );

        if (result.isEmpty()) {
            return Optional.empty();   // 해당 ID 없음
        } else {
            return Optional.of(result.get(0));
        }
    }
}
