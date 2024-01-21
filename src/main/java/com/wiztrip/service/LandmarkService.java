package com.wiztrip.service;


import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.mapstruct.LandmarkMapper;
import com.wiztrip.repository.LandmarkImageRepository;
import com.wiztrip.repository.LandmarkRepository;
import com.wiztrip.tourapi.TourApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LandmarkService {

    private final LandmarkRepository landmarkRepository;
    private final LandmarkMapper landmarkMapper;
    private final TourApiService tourApiService;
    private final LandmarkImageRepository landmarkImageRepository;

    public List<LandmarkDto.LandmarkApiResponseDto> getAllLandmarks() {
        List<LandmarkDto.LandmarkApiResponseDto> list =
            landmarkRepository.findAll().stream().map(landmarkMapper::entityToApiResponseDto).toList();
        return list;
    }

    public List<LandmarkDto.LandmarkApiResponseDto> getLandmarkByAreaCode(String areaCode) {
        List<LandmarkEntity> allByAreaCode = landmarkRepository.findAllByAreaCode(areaCode);
        return allByAreaCode.stream().map(landmarkMapper::entityToApiResponseDto).toList();
    }

    public LandmarkDto.LandmarkApiDetailResponseDto getLandmarkDetailByContentId(Long contentId) {
        LandmarkEntity landmark = landmarkRepository.findByContentId(contentId)
            .orElseThrow(() -> new CustomException(ErrorCode.LANDMARK_NOT_FOUND));
        return landmarkMapper.entityToApiDetailResponseDto(landmark);
    }

    public Page<LandmarkDto.LandmarkApiResponseDto> getLandmarkPage(Pageable pageable) {
        Page<LandmarkEntity> allByPage = landmarkRepository.findAllByPage(pageable);
        return allByPage.map(landmarkMapper::entityToApiResponseDto);
    }

    public Page<LandmarkDto.LandmarkApiResponseDto> getLandmarkPageByAreaCode(String areaCode, Pageable pageable) {
        Page<LandmarkEntity> allByPage = landmarkRepository.findAllByAreaCodeAndPage(areaCode, pageable);
        return allByPage.map(landmarkMapper::entityToApiResponseDto);
    }
}