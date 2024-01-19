package com.wiztrip.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.domain.LandmarkImageEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.mapstruct.LandmarkMapper;
import com.wiztrip.repository.LandmarkImageRepository;
import com.wiztrip.repository.LandmarkRepository;
import com.wiztrip.tourapi.ApiController;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class LandmarkService {

    private final LandmarkRepository landmarkRepository;
    private final ApiController apiController;
    private final LandmarkImageRepository landmarkImageRepository;



    // API 데이터 가져오기 및 처리
    public List<LandmarkDto.LandmarkApiResponseDto> getLandmarksFromApi(int numOfRows)
            throws URISyntaxException, JsonProcessingException {

        List<Map<String, Object>> apiData = apiController.getData(numOfRows); // 필요한 파라미터 제공

        // DTO 변환
        List<LandmarkDto.LandmarkApiResponseDto> landmarksDto = apiData.stream()
                .map(this::convertToAllLandmarkDto)
                .collect(Collectors.toList());

        List<LandmarkEntity> landmarks = landmarksDto.stream()
                .map(this::convertToLandmarkEntity)
                .collect(Collectors.toList());
        landmarkRepository.saveAll(landmarks);

        List<LandmarkImageEntity> imageEntities = landmarks.stream()
                .map(landmark -> convertToImageEntity(landmarksDto.get(landmarks.indexOf(landmark)), landmark))
                .collect(Collectors.toList());
        landmarkImageRepository.saveAll(imageEntities);

        return landmarksDto;
    }

    // 전체 여행지 불러오기(API 데이터를 LandmarkApiResponseDto 객체로 변환)
    private LandmarkDto.LandmarkApiResponseDto convertToAllLandmarkDto(Map<String, Object> apiData) {
        // API 응답 필드를 LandmarkDto 필드에 매핑
        return LandmarkDto.LandmarkApiResponseDto.builder()
                .contentId(Long.parseLong(apiData.getOrDefault("contentid", "0").toString()))
                .address((String) apiData.get("addr1")) // 'addr1'을 imageName으로 매핑
                .imagePath((String) apiData.get("firstimage")) // 'firstimage'를 imagePath로 매핑
                .contentTypeId(Long.parseLong(apiData.getOrDefault("contenttypeid", "0").toString())) // 'contenttypeid'를 contentTypeId로 매핑
                .title((String) apiData.get("title")) // 'title'을 title로 매핑
                .areaCode((String) apiData.get("areacode"))
                .build();
    }

    // 전체 여행지 (LandmarkApiResponseDto를 Landmark 엔터티로 변환)
    private LandmarkEntity convertToLandmarkEntity(LandmarkDto.LandmarkApiResponseDto dto) {
        LandmarkEntity landmark = new LandmarkEntity();

        // DTO 필드를 엔터티 필드에 매핑
        landmark.setContentId(dto.getContentId());      // 세부 여행지 확인을 위한 랜드마크의 ID
        landmark.setContentTypeId(dto.getContentTypeId());  // 관광지 타입
        landmark.setAreaCode(dto.getAreaCode());
        landmark.setName(dto.getTitle());       // 여행지 이름

        landmarkRepository.save(landmark);
        return landmark;
    }

    // api 의 이미지를 db에 저장하기 위한 로직
    public LandmarkImageEntity convertToImageEntity(LandmarkDto.LandmarkApiResponseDto dto, LandmarkEntity landmark) {
        LandmarkImageEntity imageEntity = new LandmarkImageEntity();
        imageEntity.setImagePath(dto.getImagePath());
        imageEntity.setImageName(dto.getTitle());
        imageEntity.setContentId(dto.getContentId());
        imageEntity.setLandmark(landmark);
        return imageEntity;
    }



    // 세부 여행지 불러오기(dto 변환)
    public List<LandmarkDto.LandmarkApiDetailResponseDto> getLandmarksByContentTypeId(String contentId)
            throws URISyntaxException, JsonProcessingException {

        // API 에서 데이터 가져옴
        List<Map<String, Object>> detailapiData = apiController.getLandmarkData(contentId); // 필요한 파라미터 제공
        return detailapiData.stream()
                .map(this::convertToDetailLandmarkDto)
                .collect(Collectors.toList());
    }


    // 세부 여행지 (API 데이터를 LandmarkApiDetailResponseDto 객체로 변환)
    private LandmarkDto.LandmarkApiDetailResponseDto convertToDetailLandmarkDto(Map<String, Object> detailapiData) {
        // API 응답 필드를 LandmarkDto 필드에 매핑
        return LandmarkDto.LandmarkApiDetailResponseDto.builder()
                .contentId(Long.parseLong(detailapiData.getOrDefault("contentid", "0").toString())) // 'contentid'를 contentId ㅇ 매핑
                .infocenter((String) detailapiData.get("infocenter"))
                .restDate((String) detailapiData.get("restdate"))
                .accomcount((String) detailapiData.get("accomcount"))
                .useTime((String) detailapiData.get("usetime"))
                .parking((String) detailapiData.get("parking"))
                .checkPet((String) detailapiData.get("chkpet"))
                .checkCreditCard((String) detailapiData.get("chkcreditcard"))
                .build();
    }

    // 지역기반
    public List<LandmarkDto.LandmarkApiResponseDto> getLandmarksByAreaCode(String areaCode)
            throws URISyntaxException, JsonProcessingException {

        // API 에서 데이터 가져옴
        List<Map<String, Object>> arealapiData = apiController.getAreaData(areaCode); // 필요한 파라미터 제공

        return arealapiData.stream()
                .map(this::convertToAllLandmarkDto)
                .collect(Collectors.toList());
    }

}