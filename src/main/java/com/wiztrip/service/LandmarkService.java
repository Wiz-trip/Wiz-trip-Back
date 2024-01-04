package com.wiztrip.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wiztrip.constant.Address;
import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.mapstruct.LandmarkMapper;
import com.wiztrip.repository.LandmarkRepository;
import com.wiztrip.tourapi.ApiController;
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
    private final LandmarkMapper landmarkMapper;
    private final ApiController apiController;


    // API 데이터 가져오기 및 처리
    public List<LandmarkDto.LandmarkAllResponseDto> getLandmarksFromApi(int numOfRows)
            throws URISyntaxException, JsonProcessingException {

        List<Map<String, Object>> apiData = apiController.getData(numOfRows); // 필요한 파라미터 제공
        return apiData.stream()
                .map(this::convertToLandmarkDto)
                .collect(Collectors.toList());
    }


    // 전체 여행지 불러오기
    private LandmarkDto.LandmarkAllResponseDto convertToLandmarkDto(Map<String, Object> apiData) {
        // API 응답 필드를 LandmarkDto 필드에 매핑
        return LandmarkDto.LandmarkAllResponseDto.builder()
                .landmarkId(Long.parseLong(apiData.getOrDefault("contentid", "0").toString())) // 'contentid'를 landmarkId로 매핑
                .name((String) apiData.get("title")) // 'title'을 name으로 매핑
                .imageList(Arrays.asList((String) apiData.get("firstimage"), // 'firstimage'를 imageList로 매핑
                        (String) apiData.get("firstimage2")))
                .build();
    }



    // 여행지 페이징 처리
    public Page<LandmarkEntity> getAllLandmarks(Pageable pageable) {
        return landmarkRepository.findAll(pageable);
    }


}