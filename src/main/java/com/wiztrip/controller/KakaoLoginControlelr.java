package com.wiztrip.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wiztrip.Info.KakaoResponse;
import com.wiztrip.service.KakaoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class KakaoLoginControlelr {

    private final KakaoService kakaoService;

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<?> kakaoCallback(String code) throws JsonProcessingException {
        KakaoResponse kakaoResponse = kakaoService.getKakaoUserData(code);
        return ResponseEntity.ok(kakaoResponse);

    }
}