package com.wiztrip.controller;

import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.service.LandmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/landmarks")
public class LandmarkController {

    private final LandmarkService landmarkService;

    // 여행지 조회
    @GetMapping("/landmarks")
    public ResponseEntity<List<LandmarkDto>> getAllLandmarks() {
        List<LandmarkDto> landmarks = landmarkService.getAllLandmarks();
        return ResponseEntity.ok(landmarks);
    }

    // 여행지 상세 조회
    @GetMapping("/{landmarkId}")
    public ResponseEntity<LandmarkDto> getLandmark(@PathVariable Long landmarkId) {
        LandmarkDto landmarkDto = landmarkService.getLandmarkById(landmarkId);
        return ResponseEntity.ok(landmarkDto);
    }

    // 여행지 좋아요 생성
    @PostMapping("/{landmarkId}/like")
    public ResponseEntity<Void> likeLandmark(@PathVariable Long landmarkId) {
        landmarkService.likeLandmark(landmarkId);
        return ResponseEntity.ok().build();
    }

    // 여행지 좋아요 삭제
    @DeleteMapping("/{landmarkId}/like")
    public ResponseEntity<Void> unlikeLandmark(@PathVariable Long landmarkId) {
        landmarkService.unlikeLandmark(landmarkId);
        return ResponseEntity.ok().build();
    }
}