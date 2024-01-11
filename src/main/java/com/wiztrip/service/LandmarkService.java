package com.wiztrip.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.mapstruct.LandmarkMapper;
import com.wiztrip.repository.LandmarkRepository;
import com.wiztrip.tourapi.ApiController;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LandmarkService {

    private final LandmarkRepository landmarkRepository;
    private final ApiController apiController;


    // API 데이터 가져오기 및 처리
    public List<LandmarkDto.LandmarkApiResponseDto> getLandmarksFromApi(int numOfRows)
            throws URISyntaxException, JsonProcessingException {

        List<Map<String, Object>> apiData = apiController.getData(numOfRows); // 필요한 파라미터 제공
        return apiData.stream()
                .map(this::convertToAllLandmarkDto)
                .collect(Collectors.toList());
    }

    // 전체 여행지 불러오기
    private LandmarkDto.LandmarkApiResponseDto convertToAllLandmarkDto(Map<String, Object> apiData) {
        // API 응답 필드를 LandmarkDto 필드에 매핑
        return LandmarkDto.LandmarkApiResponseDto.builder()
                .contentId(Long.parseLong(apiData.getOrDefault("contentid", "0").toString())) // 'contentid'를 imageId로 매핑
                .imageName((String) apiData.get("addr1")) // 'addr1'을 imageName으로 매핑
                .imagePath((String) apiData.get("firstimage")) // 'firstimage'를 imagePath로 매핑
                .contentTypeId(Long.parseLong(apiData.getOrDefault("contenttypeid", "0").toString())) // 'contenttypeid'를 contentTypeId로 매핑
                .title((String) apiData.get("title")) // 'title'을 title로 매핑
                .build();
    }

    // 세부 여행지 불러오기
    public List<LandmarkDto.LandmarkApiDetailResponseDto> getLandmarksByContentTypeId(String cat1,String cat2,String cat3)
            throws URISyntaxException, JsonProcessingException {

        List<Map<String, Object>> detailapiData = apiController.getLandmarkData(cat1, cat2, cat3); // 필요한 파라미터 제공
        return detailapiData.stream()
                .map(this::convertToDetailLandmarkDto)
                .collect(Collectors.toList());
    }

    private LandmarkDto.LandmarkApiDetailResponseDto convertToDetailLandmarkDto(Map<String, Object> detailapiData) {
        // API 응답 필드를 LandmarkDto 필드에 매핑
        return LandmarkDto.LandmarkApiDetailResponseDto.builder()
                .contentId(Long.parseLong(detailapiData.getOrDefault("contentid", "0").toString())) // 'contentid'를 imageId로 매핑
                .address((String) detailapiData.get("addr1")) // 'addr1'을 imageName으로 매핑
                .imagePath((String) detailapiData.get("firstimage")) // 'firstimage'를 imagePath로 매핑
                .contentTypeId(Long.parseLong(detailapiData.getOrDefault("contenttypeid", "0").toString())) // 'contenttypeid'를 contentTypeId로 매핑
                .cat1((String) detailapiData.get("cat1"))
                .cat2((String) detailapiData.get("cat2"))
                .cat3((String) detailapiData.get("cat3"))
                .title((String) detailapiData.get("title")) // 'title'을 title로 매핑
                .build();
    }

    // 여행지 페이징 처리
    public Page<LandmarkEntity> getAllLandmarks(Pageable pageable) {
        return landmarkRepository.findAll(pageable);
    }


}