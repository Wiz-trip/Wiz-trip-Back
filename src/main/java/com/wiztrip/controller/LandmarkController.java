package com.wiztrip.controller;

import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.service.LandmarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.wiztrip.dto.LandmarkLikeDto;


@RequiredArgsConstructor
@RestController
@RequestMapping("landmarks")
public class LandmarkController {

    private final LandmarkService landmarkService;

    // 모든 여행지 조회
    @Operation(summary = "모든 여행지 조회",description = "모든 여행지를 조회합니다")
    @GetMapping
    public ResponseEntity<List<LandmarkDto.LandmarkAllResponseDto>> getAllLandmarks() {
        List<LandmarkDto.LandmarkAllResponseDto> landmarks = landmarkService.getAllLandmarks();
        return ResponseEntity.ok(landmarks);
    }

    // 여행지 상세 조회
    @Operation(summary = "여행지 상세 조회",description = "여행지를 상세 조회합니다")
    @GetMapping("/{landmarkId}")
    public ResponseEntity<LandmarkDto.LandmarkDetailResponseDto> getLandmark(@PathVariable Long landmarkId) {
        LandmarkDto.LandmarkDetailResponseDto landmark = landmarkService.getLandmarkById(landmarkId);
        return ResponseEntity.ok().body(landmark);
    }

}