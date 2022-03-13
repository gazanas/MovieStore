package com.movies.store.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProperties {
    private String header = "Authorization";
    @Value(value = "${jwt.secret}")
    private String secret;
    private String tokenPrefix = "Bearer ";

    public String getHeader() {
        return header;
    }

    public String getSecret() {
        return secret;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public Date getAccessExpirationDate() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault())
                .plus(2, ChronoUnit.DAYS).toInstant());
    }

    public Date getRefreshExpirationDate() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault())
                .plus(1, ChronoUnit.YEARS).toInstant());
    }
}

