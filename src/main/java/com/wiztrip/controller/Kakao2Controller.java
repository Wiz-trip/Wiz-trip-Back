//package com.wiztrip.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wiztrip.config.spring_security.auth.OAuthToken;
//import org.springframework.http.*;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//public class Kakao2Controller {
//
//    @GetMapping("/oauth/kakao/callback")
//    public ResponseEntity<?> kakaoCallback(String code) throws JsonProcessingException {
//        RestTemplate rt = new RestTemplate();
//
//        // HttpHeaders 오브젝트 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HttpBody 오브젝트 생성
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", "c2b3cda0315bd5e42a85ba7cacb0fa43");
//        params.add("redirect_uri", "http://localhost:8080/oauth/kakao/callback");
//       // params.add("client_secret", "OeA1B2bnoMUPfFZJJI8izZLlpWZix5Bb");
//        params.add("code", code);
//
//        // HttpHeader 와 HttpBody 를 하나의 오브젝트에 담기
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
//                new HttpEntity<>(params, headers);
//
//        // Http 요청하기 - POST 방식 - response 변수의 응답을 받음
//        ResponseEntity<String> response = rt.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        // 토큰 요청에 대한 응답 처리
//        OAuthToken oAuthToken = null;
//        try {
//            oAuthToken = new ObjectMapper().readValue(response.getBody(), OAuthToken.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            // 오류 발생 시 적절한 HTTP 상태 코드와 메시지 반환
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the token");
//        }
//
//        System.out.println("카카오 accessToken :  " + oAuthToken.getAccess_token());
//        System.out.println("카카오 RefreshToken :  " + oAuthToken.getRefresh_token());
//        System.out.println("코드값 : "+ code);
//
//        // [기존 코드: 사용자 프로필 정보 요청 관련 코드]
//
//        RestTemplate rt2 = new RestTemplate();
//
//        // HttpHeaders 오브젝트 생성
//        HttpHeaders headers2 = new HttpHeaders();
//        headers2.add("Authorization","Bearer "+oAuthToken.getAccess_token() );
//        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
//
//
//        // HttpHeader 와 HttpBody 를 하나의 오브젝트에 담기
//        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest2 =
//                new HttpEntity<>(headers2);
//
//        // Http 요청하기 - POST 방식 - response 변수의 응답을 받음
//        ResponseEntity<String> response2 = rt2.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoProfileRequest2,
//                String.class
//        );
//
//
//        // 사용자 프로필 정보 요청에 대한 응답 반환
//        return ResponseEntity.ok(response.getBody());
//    }
//}
//
//
