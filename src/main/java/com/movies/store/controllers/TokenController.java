package com.movies.store.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.store.configurations.JwtProperties;
import com.movies.store.exceptions.InvalidTokenException;
import com.movies.store.exceptions.NullTokenException;
import com.movies.store.models.User;
import com.movies.store.services.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final UserService userService;

    private final JwtProperties jwtProperties;

    @ApiResponse(description = "If the authentication token is expired it refreshes it", responseCode = "200")
    @PostMapping(path = "/refresh")
    public ResponseEntity refreshToken(@RequestHeader(value = "Refresh-Token") String refreshToken) {
        if (refreshToken != null && !refreshToken.trim().isEmpty()) {
            String token = refreshToken.replace(this.jwtProperties.getTokenPrefix(), "");
            String secret = this.jwtProperties.getSecret();
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(secret.getBytes()))
                    .build().verify(token);
            String username = jwt.getSubject();
            User user = this.userService.getUserByUsername(username);
            String accessToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(this.jwtProperties.getAccessExpirationDate())
                    .sign(Algorithm.HMAC512(secret.getBytes()));

            Map<String, String> tokens = new HashMap();
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return ResponseEntity.status(HttpStatus.OK).body(objectMapper.writeValueAsString(tokens));
            } catch (JsonProcessingException e) {
                throw new InvalidTokenException("The service could not produce a new token!");
            }
        }

        throw new NullTokenException("Token can not be null");
    }
}
