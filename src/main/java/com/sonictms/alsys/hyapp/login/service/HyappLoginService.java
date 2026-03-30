package com.sonictms.alsys.hyapp.login.service;

import com.sonictms.alsys.hyapp.login.repository.HyappUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HyappLoginService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HyappUserRepository userRepository;

    public HyappLoginService(HyappUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // true: 로그인 성공, false: 로그인 실패
    public boolean login(String userId, String rawPassword) {

        return userRepository.findPasswordByUserId(userId)
                .map(encodedPwFromDb -> {
                    boolean matches = bCryptPasswordEncoder.matches(rawPassword, encodedPwFromDb);

                    System.out.println("rawPassword       :::::::::: " + rawPassword);
                    System.out.println("encodedFromDB     :::::::::: " + encodedPwFromDb);
                    System.out.println("matches() 결과   :::::::::: " + matches);

                    return matches;
                })
                .orElse(false);
    }
}
