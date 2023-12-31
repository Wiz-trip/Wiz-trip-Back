package com.wiztrip.controller;

import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.service.LandmarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("landmarks")
public class LandmarkController {

    private final LandmarkService landmarkService;

    // 모든 여행지 조회
    @Operation(summary = "모든 여행지 조회",description = "모든 여행지를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<LandmarkDto.LandmarkAllResponseDto>> getAllLandmarks() {
        List<LandmarkDto.LandmarkAllResponseDto> landmarks = landmarkService.getAllLandmarks();
        return ResponseEntity.ok(landmarks);
    }

    // 여행지 상세 조회
    @Operation(summary = "상세 여행지 조회",description = "landmarkId 를 사용하여 상세 여행지를 조회합니다.")
    @GetMapping("/{landmarkId}")
    public ResponseEntity<LandmarkDto.LandmarkDetailResponseDto> getLandmark(@PathVariable Long landmarkId) {
        LandmarkDto.LandmarkDetailResponseDto landmark = landmarkService.getLandmarkById(landmarkId);
        return ResponseEntity.ok().body(landmark);
    }



    // 페이징
    @Operation(summary = "여행지 페이징",
            description = """
            content: 페이지 요청에 의해 반환된 랜드마크(또는 기타 엔터티)의 실제 목록. 이 목록의 각 항목은 데이터베이스의 하나의 레코드를 나타냄
            
            pageable: 정렬 순서, 페이지 번호 및 페이지 크기를 포함하여 페이지 매김에 대한 세부 정보를 포함
            
            totalPages: 사용 가능한 총 페이지 수
            
            totalElements: 전체 데이터세트의 총 요소 또는 레코드 수
            
            size, number: 현재 페이지 크기와 페이지 번호를 반영
            
            first, last: 현재 페이지가 첫 번째인지 마지막인지 나타내는 값
            
            sort: 데이터에 적용된 정렬에 대한 정보
            
            empty: 현재 페이지가 비어 있는지 여부를 나타냄
            """)
    @GetMapping("/page")
    public ResponseEntity<Page<LandmarkEntity>> getAllLandmarks(
            // 각 페이지의 크기는 10으로 설정
            // landmark id로 일단 정렬하는 방식 사용
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<LandmarkEntity> landmarks = landmarkService.getAllLandmarks(pageable);
        return ResponseEntity.ok(landmarks);
    }

}