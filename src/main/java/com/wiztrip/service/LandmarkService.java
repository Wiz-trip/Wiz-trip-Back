package com.wiztrip.service;

import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.repository.LandmarkLikeRepository;
import com.wiztrip.repository.LandmarkRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LandmarkService {

    private final LandmarkRepository landmarkRepository;
    private final LandmarkLikeRepository landmarkLikeRepository;


    // 모든 랜드마크 조회
    public List<LandmarkDto> getAllLandmarks() {
        return landmarkRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID로 랜드마크 조회
    public LandmarkDto getLandmarkById(Long landmarkId) {
        LandmarkEntity landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new EntityNotFoundException("Landmark not found with id: " + landmarkId));
        return convertToDto(landmark);
    }

    // 랜드마크에 좋아요
    public void likeLandmark(Long landmarkId) {
        // 좋아요 로직 구현
        // 예시: LandmarkLikeEntity 생성 및 저장
    }

    // 랜드마크 좋아요 취소
    public void unlikeLandmark(Long landmarkId) {
        // 좋아요 취소 로직 구현
        // 예시: LandmarkLikeEntity 삭제
    }

    // LandmarkEntity를 LandmarkDto로 변환
    private LandmarkDto convertToDto(LandmarkEntity landmark) {
        LandmarkDto dto = new LandmarkDto();
        dto.setId(landmark.getId());
        dto.setName(landmark.getName());
        dto.setAddress(landmark.getAddress());
        dto.setImageList(landmark.getImageList());
        return dto;
    }
}
