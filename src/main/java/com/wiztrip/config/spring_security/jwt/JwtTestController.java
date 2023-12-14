package com.wiztrip.config.spring_security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("jwt-test")
public class JwtTestController {

    @PostMapping("/getToken")
    public ResponseEntity<String> returnAuthentication(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("jwt: ").append(request.getHeader(JwtProperties.jwtHeaderString));
        sb.append("\n");
        sb.append("refresh: ").append(request.getHeader(JwtProperties.refreshHeaderString));

        return ResponseEntity.ok().body(sb.toString());
    }
}
