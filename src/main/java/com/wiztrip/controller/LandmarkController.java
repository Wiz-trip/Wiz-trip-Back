package com.wiztrip.controller;


import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.service.LandmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("landmarks")
public class LandmarkController {

    private final LandmarkService landmarkService;

    // 모든 여행지 조회
    @GetMapping
    public ResponseEntity<List<LandmarkDto.LandmarkAllResponseDto>> getAllLandmarks() {
        List<LandmarkDto.LandmarkAllResponseDto> landmarks = landmarkService.getAllLandmarks();
        return ResponseEntity.ok(landmarks);
    }

    // 여행지 상세 조회
    @GetMapping("/{landmarkId}")
    public ResponseEntity<LandmarkDto.LandmarkDetailResponseDto> getLandmark(@PathVariable Long landmarkId) {
        LandmarkDto.LandmarkDetailResponseDto landmark = landmarkService.getLandmarkById(landmarkId);
        return ResponseEntity.ok().body(landmark);
    }

}