package com.wiztrip.config.spring_security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.config.spring_security.auth.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenUtils {

    private final PrincipalDetailsService principalDetailsService;

    public String createToken(PrincipalDetails principalDetails, long expirationTime, String secret) {
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUsername())
                .sign(Algorithm.HMAC512(secret));
    }

    public Map<String, String> updateTokenByRefreshToken(String refresh) {

        String username = JWT.require(Algorithm.HMAC512(JwtProperties.refreshSecret)).build().verify(refresh)
                .getClaim("username").asString();

        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("jwt", createToken(principalDetails,
                JwtProperties.jwtExpirationTime, JwtProperties.jwtSecret));
        tokens.put("refresh", createToken(principalDetails,
                JwtProperties.refreshExpirationTime, JwtProperties.refreshSecret));

        return tokens;

    }
}
