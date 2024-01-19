package com.wiztrip.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.repository.LandmarkRepository;
import com.wiztrip.service.LandmarkService;
import com.wiztrip.tourapi.ApiController;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("landmarks")
public class LandmarkController {

    private final LandmarkService landmarkService;
    private final LandmarkRepository landmarkRepository;
    private final ApiController apiController;


    @Operation(summary = "모든 관광지 조회", description =
            """
            관광타입 (12-> 관광지) 로 필터링 한 모든 관광지를 보여줍니다. 
            한 페이지의 결과수는 100개로 하였습니다.         
            """
    )
    @GetMapping
    public ResponseEntity<List<LandmarkDto.LandmarkApiResponseDto>> getAllLandmarks() {
        try {
            List<LandmarkDto.LandmarkApiResponseDto> landmarks = landmarkService.getLandmarksFromApi(100);
            return ResponseEntity.ok(landmarks);
        } catch (Exception e) {
            // 오류 처리, 예를 들어 API 호출 실패
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 지역기반 검색
    @Operation(summary = "지역기반 관광지 조회", description =
                         """
                             * areaCode
                             서울 : 1
                             인천 : 2
                             대전 : 3
                             대구 : 4
                             광주 : 5
                             부산 : 6
                             울산 : 7
                             세종 : 8
                             경기 : 31
                             강원 : 32
                             충북 : 33
                             충남 : 34
                             경북 : 35
                             경남 : 36
                             전북 : 37
                             전남 : 38
                             제주 : 39
                     """
    )
    @GetMapping("/landmarksAreaCode")
    public ResponseEntity<List<LandmarkDto.LandmarkApiResponseDto>> getLandmarksByAreaCode(
            @RequestParam String areaCode)
            throws URISyntaxException, JsonProcessingException {
        List<LandmarkDto.LandmarkApiResponseDto> landmarks = landmarkService.getLandmarksByAreaCode(areaCode);
        return ResponseEntity.ok(landmarks);
    }


    @Operation(summary = "상세 관광지 조회",
            description = """
                    < 여행지의 contentId 로 상세 여행지의 소개정보를 조회 >
                    * 여행지의 전화번호
                    * 쉬는날
                    * 수용인원
                    * 영업 시간
                    * 주차 가능 여부
                    * 애완동물 출입 가능 여부
                    * 신용카드 가능 여부
                    
                    를 조회
                   
                    """
                    )
    @GetMapping("/landmarks")
    public ResponseEntity<List<LandmarkDto.LandmarkApiDetailResponseDto>> getLandmarksByContentTypeId(
            @RequestParam String contentId)
            throws URISyntaxException, JsonProcessingException {
        List<LandmarkDto.LandmarkApiDetailResponseDto> landmarks = landmarkService.getLandmarksByContentTypeId(contentId);
        return ResponseEntity.ok(landmarks);
    }


    // 페이징
    @Operation(summary = "여행지 페이징",description = "numOfRows : 보여질 데이터 개수, pageNo : 페이지 number")
    @GetMapping("/landmarks/paging")
    public Page<Map<String, Object>> getLandmarks(
            @RequestParam(value = "numOfRows", defaultValue = "10") int numOfRows,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) throws Exception {


        List<Map<String, Object>> paginatedList = apiController.pagingData(numOfRows , pageNo);

        // Page 객체 반환
        return new PageImpl<>(paginatedList, PageRequest.of(pageNo - 1, numOfRows), paginatedList.size());
    }

}