package com.wiztrip.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {       // 나중에 지우면 됌

    @Operation(summary = "카톡 로그인 확인",description = "카톡 로그인 확인용")
    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
        // 로그인 성공 처리
        // 사용자 정보를 oauth2User에서 추출하여 사용
        return "로그인 성공";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        // 로그인 실패 처리
        return "로그인 실패";
    }
}
