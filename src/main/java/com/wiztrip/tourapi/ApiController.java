package com.wiztrip.tourapi;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@Slf4j
public class ApiController {

    public List<Map<String, Object>> getData(int numOfRows)
            throws URISyntaxException, JsonProcessingException {

        // Base URL + API 호출 주소
        String link = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1";
        String MobileOS = "ETC";        // 실행환경
        String MobileApp = "Test";      // APP name
        String _type = "json";          // 받을 데이터 타입

        // 서비스 키
        String serviceKey = "FI6qPw0hnomFTdMepcDdUiUO1wVBjkwNyUrPJxdCTP1SVxDMnBOb0LWcjrGyAi8Mz4zzC%2B9yH1RH8Twh1rIrdA%3D%3D";

        String url = link + "?" +
                "&MobileOS=" + MobileOS +
                "&MobileApp=" + MobileApp +
                "&_type=" + _type +
//                "&areaCode=" + areaCode +               // 받아온 지역 코드
//                "&contentTypeId=" + contentTypeId +     // 받아온 관광 타입
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
//                .filter(item -> {
//                    Object value = item.get("addr1");
//                    return value != null && value.toString().contains(state);
//                })
                .collect(Collectors.toList());


        return testItemMap;
    }


    // 세부 여행지 메서드
    public Map<String, Object> getLandmarkData(long contentTypeId)
            throws URISyntaxException, JsonProcessingException {

        // Base URL and API parameters
        String link = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1";// Assuming a different endpoint for detailed data
        String serviceKey = "FI6qPw0hnomFTdMepcDdUiUO1wVBjkwNyUrPJxdCTP1SVxDMnBOb0LWcjrGyAi8Mz4zzC%2B9yH1RH8Twh1rIrdA%3D%3D";
        String MobileOS = "ETC";        // 실행환경
        String MobileApp = "Test";      // APP name
        String _type = "json";          // 받을 데이터 타입

        String url = link + "?" +
                "serviceKey=" + serviceKey +
                "&contentTypeId=" + contentTypeId +
                "&MobileOS=" + MobileOS +
                "&MobileApp=" + MobileApp +
                "&_type=" + _type;


        URI uri = new URI(url);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // API Request
        String response = restTemplate.getForObject(uri, String.class);
        log.info("Detail API Response: {}", response);

        // Data Extraction
        Map<String, Object> map = new ObjectMapper().readValue(response, Map.class);
        Map<String, Object> responseMap = (Map<String, Object>) map.get("response");
        Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
        Map<String, Object> itemMap = (Map<String, Object>) bodyMap.get("item");

        return itemMap;
    }


}
