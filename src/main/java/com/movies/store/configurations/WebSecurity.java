package com.movies.store.configurations;

import com.movies.store.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final MovieUserService movieUserService;

    private final UserRepository userRepository;

    private final JwtProperties jwtProperties;

    /**
     * Define the way the user is going to be retrieved from the database
     * for authentication and how the password will be hashed
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(movieUserService).passwordEncoder(encoder());
    }

    /**
     * Add request filters and add security on the endpoints
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), this.jwtProperties))
                .addFilterBefore(new JwtAuthorizationFilter(this.userRepository, this.jwtProperties),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/users/register", "/api/v1/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/movies/available", "/api/v1/movies/search").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/copies/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/charges/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/copies/movie/{movie_id}/rent").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/v1/copies/{copy_id}/return").authenticated();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cors configuration bean definition
     *
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfiguration() {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return corsConfigurationSource;
    }
}
