package com.wiztrip.controller;

import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.service.LandmarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("landmarks")
public class LandmarkController {
    private final LandmarkService landmarkService;


    @Operation(summary = "모든 관광지 조회", description =
        """
            관광타입 (12-> 관광지) 로 필터링 한 모든 관광지를 보여줍니다.
            """
    )
    @GetMapping
    public ResponseEntity<List<LandmarkDto.LandmarkApiResponseDto>> getAllLandmarks() {
        return ResponseEntity.ok().body(landmarkService.getAllLandmarks());
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
        @RequestParam String areaCode) {
        return ResponseEntity.ok().body(landmarkService.getLandmarkByAreaCode(areaCode));
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
    public ResponseEntity<LandmarkDto.LandmarkApiDetailResponseDto> getLandmarksByContentTypeId(
        @RequestParam String contentId) {
        return ResponseEntity.ok().body(landmarkService.getLandmarkDetailByContentId(Long.parseLong(contentId)));
    }


    // 페이징
    @Operation(summary = "여행지 페이징", description = "numOfRows : 보여질 데이터 개수, pageNo : 페이지 number 전체 관광지 페이징 조회")
    @GetMapping("/landmarks/paging")
    public ResponseEntity<Page<LandmarkDto.LandmarkApiResponseDto>> getLandmarks(
        @RequestParam(value = "numOfRows", defaultValue = "10") int numOfRows,
        @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "name") String... sortBy
    ) {
        Sort.Direction sortDirection = Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(pageNo, numOfRows, sortDirection, sortBy);
        return ResponseEntity.ok().body(landmarkService.getLandmarkPage(pageRequest));
    }

    // 페이징
    @Operation(summary = "여행지 페이징 - 지역 기반", description = "numOfRows : 보여질 데이터 개수, pageNo : 페이지 number 지역기반 관광지 페이징 조회")
    @GetMapping("/landmarksAreaCode/paging")
    public ResponseEntity<Page<LandmarkDto.LandmarkApiResponseDto>> getLandmarksByAreaCode(
        @RequestParam String areaCode,
        @RequestParam(value = "numOfRows", defaultValue = "10") int numOfRows,
        @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "name") String... sortBy
    ) {
        Sort.Direction sortDirection = Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(pageNo, numOfRows, sortDirection, sortBy);
        return ResponseEntity.ok().body(landmarkService.getLandmarkPageByAreaCode(areaCode, pageRequest));
    }
}
