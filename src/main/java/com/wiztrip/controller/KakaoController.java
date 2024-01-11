//package com.wiztrip.controller;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wiztrip.config.spring_security.auth.OAuthToken;
//import org.springdoc.core.configuration.oauth2.SpringDocOAuth2Token;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//public class KakaoController {
//    @GetMapping("/oauth/kakao/callback")
//    public ResponseEntity<?> getAccessToken(@RequestParam("code") String code) {
//        System.out.println("code : " + code);
//
//        // 1. header 생성
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
//
//        // 2. body 생성
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", "c2b3cda0315bd5e42a85ba7cacb0fa43");
//        params.add("redirect_uri", "http://localhost:8080/oauth/kakao/callback");
//        params.add("client_secret", "OeA1B2bnoMUPfFZJJI8izZLlpWZix5Bb");
//        params.add("code", code);
//
//        // 3. header + body
//        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, httpHeaders);
//
//        // 4. http 요청하기
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Object> response = restTemplate.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                httpEntity,
//                Object.class
//        );
//
//        System.out.println("response = " + response);
//
//        // return ResponseEntity.ok(response.getBody());
//
//
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        OAuthToken oAuthToken = null;
//        try {
//            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        // HttpHeaders 오브젝트 생성
//        RestTemplate rt2 = new RestTemplate();
//        MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
//        params2.add("Authorization", "Bearer" + oAuthToken.getAccess_token());
//        params2.add("Content", "application/x-www-form-urlencoded;charset=utf-8");
//
//
//        // HttpHeader 와 HttpBody 를 하나의 오브젝트에 담기
//        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest2 =
//                new HttpEntity<>(params2);
//
//        // Http 요청하기 - POST 방식 - response 변수의 응답을 받음
//        ResponseEntity<String> response2 = rt2.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoProfileRequest2,
//                String.class
//        );
//
//    return ResponseEntity.ok(response2.getBody());
//    }
//}
