package com.wiztrip.service;

import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.repository.LandmarkLikeRepository;
import com.wiztrip.repository.LandmarkRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LandmarkService {

    private final LandmarkRepository landmarkRepository;
    private final LandmarkLikeRepository landmarkLikeRepository;


    // 모든 여행지 조회
    public List<LandmarkEntity> getAllLandmarks() {
        return landmarkRepository.findAll();
    }

    // 여행지 상세 조회
    public LandmarkDto getLandmarkById(Long landmarkId) {
        LandmarkEntity landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new EntityNotFoundException("Landmark 의 id를 찾을 수 없습니다 : " + landmarkId));
        return convertToDto(landmark);
    }

    // LandmarkEntity를 LandmarkDto로 변환
    private LandmarkDto convertToDto(LandmarkEntity landmarkEntity) {
        // LandmarkEntity의 필드를 LandmarkDto에 매핑
        return new LandmarkDto();
    }

}
