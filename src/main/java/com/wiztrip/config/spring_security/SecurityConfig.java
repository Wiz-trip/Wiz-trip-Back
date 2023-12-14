package com.wiztrip.config.spring_security;

import com.wiztrip.config.spring_security.auth.PrincipalDetailsService;
import com.wiztrip.config.spring_security.jwt.JwtAuthenticationFilter;
import com.wiztrip.config.spring_security.jwt.JwtAuthorizationFilter;
import com.wiztrip.config.spring_security.jwt.JwtExceptionHandlerFilter;
import com.wiztrip.config.spring_security.jwt.TokenUtils;
import com.wiztrip.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrincipalDetailsService principalDetailsService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilter(corsConfig.corsFilter())
                .addFilter(new JwtAuthenticationFilter(authenticationManager, tokenUtils))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository, principalDetailsService, tokenUtils))
                .addFilterBefore(new JwtExceptionHandlerFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("jwt-test/**")
                        .authenticated()
                        .anyRequest().permitAll())
                .build();
    }
}
