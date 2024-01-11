package com.wiztrip.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiztrip.Info.KakaoResponse;
import com.wiztrip.Info.KakaoTokenIfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.Map;

@RestController
public class KakaoLoginControlelr {

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<?> kakaoCallback(String code) throws JsonProcessingException {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "c2b3cda0315bd5e42a85ba7cacb0fa43"); // 여기에 클라이언트 ID를 입력하세요
        params.add("redirect_uri", "http://localhost:8080/oauth/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoTokenIfo tokenInfo = objectMapper.readValue(response.getBody(), KakaoTokenIfo.class);

        // 사용자 정보 요청
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + tokenInfo.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        Map<String, Object> userInfo = objectMapper.readValue(response2.getBody(), new TypeReference<Map<String, Object>>() {});

        // 최종 응답 객체 생성 및 반환
        KakaoResponse kakaoResponse = new KakaoResponse();

        kakaoResponse.setAccess_Token(tokenInfo.getAccess_token());
        kakaoResponse.setRefresh_Token(tokenInfo.getRefresh_token());
        kakaoResponse.setUserInfo(userInfo);

        return ResponseEntity.ok(kakaoResponse);
    }

}
