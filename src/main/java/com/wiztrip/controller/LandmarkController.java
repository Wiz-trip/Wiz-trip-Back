package com.wiztrip.controller;

import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.service.LandmarkService;
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
    @GetMapping
    public ResponseEntity<List<LandmarkEntity>> getAllLandmarks() {
        List<LandmarkEntity> landmarks = landmarkService.getAllLandmarks();

        return ResponseEntity.ok(landmarks);
    }

    // 여행지 상세 조회
    @GetMapping("/{landmarkId}")
    public ResponseEntity<LandmarkDto> getLandmark(@RequestParam Long landmarkId) {
        return ResponseEntity.ok().body(landmarkService.getLandmarkById(landmarkId));
    }
}