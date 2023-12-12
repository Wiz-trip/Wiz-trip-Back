package com.wiztrip.service;

import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.dto.LandmarkLikeDto;
import com.wiztrip.mapstruct.LandmarkMapper;
import com.wiztrip.repository.LandmarkLikeRepository;
import com.wiztrip.repository.LandmarkRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LandmarkService {

    private final LandmarkRepository landmarkRepository;
    private final LandmarkMapper landmarkMapper;


    // 모든 여행지 조회
    public List<LandmarkDto.LandmarkAllResponseDto> getAllLandmarks() {
        return landmarkRepository.findAll().stream()
                .map(landmarkMapper::entityToAllResponseDto) // 'entityToLandmarkAllResponseDto' 메서드 사용
                .collect(Collectors.toList());
    }

    // 여행지 상세 조회
    public LandmarkDto.LandmarkDetailResponseDto getLandmarkById(Long landmarkId) {
        LandmarkEntity landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new EntityNotFoundException("Landmark 의 id를 찾을 수 없습니다 : " + landmarkId));
        return landmarkMapper.entityToDetailResponseDto(landmark); // 'entityToLandmarkDetailResponseDto' 메서드 사용
    }


}