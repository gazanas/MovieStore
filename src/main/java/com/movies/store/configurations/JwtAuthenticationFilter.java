package com.movies.store.configurations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.store.dtos.AuthenticationDto;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties) {
        this.authenticationManager = authenticationManager;
        this.jwtProperties = jwtProperties;
        super.setFilterProcessesUrl("/api/v1/login");
    }

    /**
     * Attempt authentication using the credentials send by the user
     *
     * @param request The authentication request
     * @param response The authentication response
     * @return The authentication result
     * @throws AuthenticationException
     */
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthenticationDto loginData = new ObjectMapper().readValue(request.getInputStream(), AuthenticationDto.class);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginData.getUsername(),
                loginData.getPassword(),
                List.of()
        );

        return this.authenticationManager.authenticate(authenticationToken);
    }

    /**
     * When the authentication is unsuccessful send the correct response
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(failed.getLocalizedMessage());
    }

    /**
     * When authenticated successfully then create an access json web token for authorization
     * and a refresh json web token for refreshing the access json web token when it's expired
     * and send both of them as the response body.
     *
     * @param request The authentication request
     * @param response The authentication response
     * @param chain The filter chain
     * @param authResult The authentication result
     * @throws IOException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User principal = (User) authResult.getPrincipal();
        String secret = this.jwtProperties.getSecret();

        String accessToken = JWT.create().withSubject(principal.getUsername())
                .withExpiresAt(this.jwtProperties.getAccessExpirationDate())
                .sign(Algorithm.HMAC512(secret.getBytes()));

        String refreshToken = JWT.create().withSubject(principal.getUsername())
                .withExpiresAt(this.jwtProperties.getRefreshExpirationDate())
                .sign(Algorithm.HMAC512(secret.getBytes()));

        Map<String, String> authResponse = new HashMap<>();
        authResponse.put("access_token", accessToken);
        authResponse.put("refresh_token", refreshToken);
        response.getWriter().write(new ObjectMapper().writeValueAsString(authResponse));
    }
}
