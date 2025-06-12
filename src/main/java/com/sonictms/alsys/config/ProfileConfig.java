package com.sonictms.alsys.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ProfileConfig {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public String getProfileDisplayName() {
        switch (activeProfile) {
            case "prod":
                return "운영";
            case "dev":
                return "개발";
            default:
                return activeProfile;
        }
    }
}