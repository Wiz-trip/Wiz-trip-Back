package com.wiztrip.config.spring_security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    protected static String jwtSecret;
    protected static String refreshSecret;
    protected static Long jwtExpirationTime;
    protected static Long refreshExpirationTime;
    protected static String tokenPrefix;
    protected static String jwtHeaderString;
    protected static String refreshHeaderString;

    @Value("${jwt.jwt-secret}")
    public void setJwtSecret(String secret) {
        JwtProperties.jwtSecret = secret;
    }

    @Value("${jwt.refresh-secret}")
    public void setRefreshSecret(String secret) {
        JwtProperties.refreshSecret = secret;
    }

    @Value("${jwt.jwt-expiration-time}") //10일
    public void setJwtExpirationTime(Long expirationTime) {
        JwtProperties.jwtExpirationTime = expirationTime;
    }

    @Value("${jwt.refresh-expiration-time}") //10일
    public void setRefreshExpirationTime(Long expirationTime) {
        JwtProperties.refreshExpirationTime = expirationTime;
    }

    @Value("${jwt.token-prefix}")
    public void setTokenPrefix(String tokenPrefix) {
        JwtProperties.tokenPrefix = tokenPrefix+" ";
    }

    @Value("${jwt.jwt-header-string}")
    public void setHeaderString(String headerString) {
        JwtProperties.jwtHeaderString = headerString;
    }

    @Value("${jwt.refresh-header-string}")
    public void setRefreshHeaderString(String headerString) {
        JwtProperties.refreshHeaderString = headerString;
    }
}
