package com.movies.store.configurations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.store.exceptions.InvalidTokenException;
import com.movies.store.models.User;
import com.movies.store.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtAuthorizationFilter extends OncePerRequestFilter {


    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    public JwtAuthorizationFilter(UserRepository userRepository, JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
    }


    /**
     * Attempt authorization for the current user, by getting the authorization header.
     * If the header is not a valid json web token format then deny the authorization.
     * If the user is authorized continue with the request else return the thrown exception
     * message as the body of the response.
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(this.jwtProperties.getHeader());

        if (header == null || !header.startsWith(this.jwtProperties.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = this.getUsernamePasswordAuthentication(header);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (TokenExpiredException e) {
            Map<String, String> error = new HashMap();
            error.put("message", "Token has expired");
            error.put("status", Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            error.put("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(objectMapper.writeValueAsString(error));
            return;
        } catch (SignatureVerificationException e) {
            Map<String, String> error = new HashMap();
            error.put("message", "Token could not be verified");
            error.put("status", Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            error.put("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(objectMapper.writeValueAsString(error));
            return;
        } catch (InvalidTokenException e) {
            Map<String, String> error = new HashMap();
            error.put("message", e.getMessage());
            error.put("status", Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            error.put("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(objectMapper.writeValueAsString(error));
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Verify the json web token and retrieve the username (subject) from it.
     * Return an authentication token containing the information of the authorized user.
     * If the user is not authorized return null.
     *
     * @param authorizationHeader The authorization header
     * @return The authentication token if the user is authorized else null
     */
    private Authentication getUsernamePasswordAuthentication(String authorizationHeader) {
        String token = authorizationHeader.replace(this.jwtProperties.getTokenPrefix(), "");

        String username = null;
        if (token != null) {
            username = JWT.require(Algorithm.HMAC512(this.jwtProperties.getSecret().getBytes()))
                    .build().verify(token).getSubject();
        }

        if (username != null) {
            User user = this.userRepository.findByUsername(username)
                    .orElseThrow(() -> new InvalidTokenException("Invalid Authentication Token"));

            org.springframework.security.core.userdetails.User principal =
                    new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), List.of());
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
                    principal.getAuthorities());

            return auth;
        }

        return null;
    }
}
