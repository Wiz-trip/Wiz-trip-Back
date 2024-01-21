package com.wiztrip.tourapi;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class TourApiTool {

    @Value("${service.key}")
    private String serviceKey;



    public List<Map<String, Object>> getData(int numOfRows)
            throws URISyntaxException, JsonProcessingException {

        // Base URL + API 호출 주소
        String link = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1";
        String MobileOS = "ETC";        // 실행환경
        String MobileApp = "WizTripWeb";      // APP name
        String _type = "json";          // 받을 데이터 타입
        String contentTypeId = "12";    // 관광지(12)

        String url = link + "?" +
                "&MobileOS=" + MobileOS +
                "&MobileApp=" + MobileApp +
                "&_type=" + _type +
                "&contentTypeId=" + contentTypeId +     // 받아온 관광 타입
                "&numOfRows=" + numOfRows +             // 출력할 데이터 개수
                "&serviceKey=" + serviceKey;

        URI uri = new URI(url);         // 작선한 문자열로 URL 생성
        RestTemplate restTemplate = new RestTemplate();     // HTTP 요청 수행
        HttpHeaders headers = new HttpHeaders();            // HTTP 요청 헤더 생성

        // content-type 으로 데이터 타입 지정 , 여기서는 UTF-8
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // get 요청
        String response = restTemplate.getForObject(
                uri,            // 요청 보낼 url
                String.class    // 응답을 문자열로 받겠음.
        );
        // 로그 출력
        log.info("API Response: {}", response);

        // 데이터 추출
        Map<String, Object> map = new ObjectMapper().readValue(response.toString(), Map.class);
        Map<String, Object> responseMap = (Map<String, Object>) map.get("response");
        Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
        Map<String, Object> itemsMap = (Map<String, Object>) bodyMap.get("items");
        List<Map<String, Object>> itemMap = (List<Map<String, Object>>) itemsMap.get("item");

        //state에 있는 정보만 들고오기
        List<Map<String, Object>> testItemMap = itemMap.stream()
                .collect(Collectors.toList());
        return testItemMap;
    }

    public List<Map<String, Object>> getAreaData(String areaCode)
            throws URISyntaxException, JsonProcessingException {

        // Base URL + API 호출 주소
        String link = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1";
        String MobileOS = "ETC";        // 실행환경
        String MobileApp = "WizTripWeb";      // APP name
        String _type = "json";          // 받을 데이터 타입
        String contentTypeId = "12";    // 관광지(12)

        String url = link + "?" +
                "&MobileOS=" + MobileOS +
                "&MobileApp=" + MobileApp +
                "&_type=" + _type +
                "&contentTypeId=" + contentTypeId +     // 받아온 관광 타입
                "&areaCode=" + areaCode +             // 출력할 데이터 개수
                "&serviceKey=" + serviceKey;

        URI uri = new URI(url);         // 작선한 문자열로 URL 생성
        RestTemplate restTemplate = new RestTemplate();     // HTTP 요청 수행
        HttpHeaders headers = new HttpHeaders();            // HTTP 요청 헤더 생성

        // content-type 으로 데이터 타입 지정 , 여기서는 UTF-8
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // get 요청
        String response = restTemplate.getForObject(
                uri,            // 요청 보낼 url
                String.class    // 응답을 문자열로 받겠음.
        );


        // 데이터 추출
        Map<String, Object> map = new ObjectMapper().readValue(response.toString(), Map.class);
        Map<String, Object> responseMap = (Map<String, Object>) map.get("response");
        Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
        Map<String, Object> itemsMap = (Map<String, Object>) bodyMap.get("items");
        List<Map<String, Object>> itemMap = (List<Map<String, Object>>) itemsMap.get("item");

        //state에 있는 정보만 들고오기
        List<Map<String, Object>> testItemMap = itemMap.stream()
                .collect(Collectors.toList());
        return testItemMap;
    }





    // 세부 여행지 메서드
    public Map<String, Object> getLandmarkData(String contentId)
            throws URISyntaxException, JsonProcessingException {

        // Base URL + API 호출 주소
        String link = "https://apis.data.go.kr/B551011/KorService1/detailIntro1";
        String MobileOS = "ETC";        // 실행환경
        String MobileApp = "Test";      // APP name
        String _type = "json";          // 받을 데이터 타입
        String contentTypeId = "12";    // 관광지(12)
        int numOfRows = 1;
        int pageNo = 1;

        String url = link + "?" +
                "&MobileOS=" + MobileOS +
                "&MobileApp=" + MobileApp +
                "&_type=" + _type +
                "&numOfRows=" + numOfRows +
                "&pageNo=" + pageNo +
                "&contentTypeId=" + contentTypeId +
                "&contentId=" + contentId +
                "&serviceKey=" + serviceKey;

        URI uri = new URI(url);         // 작선한 문자열로 URL 생성
        RestTemplate restTemplate = new RestTemplate();     // HTTP 요청 수행
        HttpHeaders headers2 = new HttpHeaders();            // HTTP 요청 헤더 생성

        // content-type 으로 데이터 타입 지정 , 여기서는 UTF-8
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // get 요청
        String response = restTemplate.getForObject(
                uri,            // 요청 보낼 url
                String.class    // 응답을 문자열로 받겠음.
        );
        // 로그 출력
        log.info("Detail API Response: {}", response);

        // 데이터 추출
        Map<String, Object> map = new ObjectMapper().readValue(response.toString(), Map.class);
        Map<String, Object> responseMap = (Map<String, Object>) map.get("response");
        Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
        Map<String, Object> itemsMap = (Map<String, Object>) bodyMap.get("items");
        List<Map<String,Object>> itemMap = (List<Map<String, Object>>) itemsMap.get("item");

        //state에 있는 정보만 들고오기
        return itemMap.get(0);
    }

    // 페이징
//    public List<Map<String, Object>> pagingData(int numOfRows,int pageNo)
//            throws Exception {
//
//        // API 호출을 위한 기본 설정
//        String link = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1";
//        String mobileOS = "ETC";
//        String mobileApp = "Test";
//        String _type = "json";
//        String contentTypeId = "12"; // 관광지 타입
//
//        // URL 생성
//        String url = link + "?serviceKey=" + serviceKey +
//                "&MobileOS=" + mobileOS +
//                "&MobileApp=" + mobileApp +
//                "&_type=" + _type +
//                "&contentTypeId=" + contentTypeId +
//                "&pageNo=" +pageNo +
//                "&numOfRows=" + numOfRows;
//
//        // HTTP 요청 수행
//        RestTemplate restTemplate = new RestTemplate();
//        URI uri = new URI(url);
//        String response = restTemplate.getForObject(uri, String.class);
//
//        Map<String, Object> map = new ObjectMapper().readValue(response.toString(), Map.class);
//        Map<String, Object> responseMap = (Map<String, Object>) map.get("response");
//        Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
//        Map<String, Object> itemsMap = (Map<String, Object>) bodyMap.get("items");
//        List<Map<String, Object>> itemMap = (List<Map<String, Object>>) itemsMap.get("item");
//
//        return itemMap.stream()
//                .collect(Collectors.toList());
//    }

}
