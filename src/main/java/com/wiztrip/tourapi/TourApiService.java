package com.wiztrip.tourapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wiztrip.constant.Address;
import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.domain.LandmarkImageEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.repository.LandmarkImageRepository;
import com.wiztrip.repository.LandmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourApiService {

    private final LandmarkRepository landmarkRepository;
    private final TourApiTool tourApiTool;
    private final LandmarkImageRepository landmarkImageRepository;


    // API 데이터 가져오기 및 처리
    public List<LandmarkEntity> getLandmarksFromApi(int numOfRows)
        throws URISyntaxException, JsonProcessingException {

        List<Map<String, Object>> apiData = tourApiTool.getData(numOfRows); // 필요한 파라미터 제공

        // DTO 변환
        List<LandmarkDto.LandmarkApiResponseDto> landmarksDto = apiData.stream()
            .map(this::convertToAllLandmarkDto)
            .collect(Collectors.toList());

        List<LandmarkEntity> landmarks = landmarksDto.stream()
            .map(o -> {
                LandmarkEntity landmark = convertToLandmarkEntity(o);
                landmark.setAddress(new Address(o.getAddress(), null));

                landmark.getImageList().add(convertToImageEntity(o, landmark));

                LandmarkDto.LandmarkApiDetailResponseDto landmarkDetail = null;
                try {
                    landmarkDetail = getLandmarksByContentTypeId(String.valueOf(o.getContentId()));
                } catch (URISyntaxException | JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                landmark.setInfocenter(landmarkDetail.getInfocenter());
                landmark.setRestDate(landmarkDetail.getRestDate());
                landmark.setAccomcount(landmarkDetail.getAccomcount());
                landmark.setUseTime(landmarkDetail.getUseTime());
                landmark.setParking(landmarkDetail.getParking());
                landmark.setCheckPet(landmarkDetail.getCheckPet());
                landmark.setCheckCreditCard(landmarkDetail.getCheckCreditCard());

                return landmark;
            })
            .collect(Collectors.toList());

        landmarkRepository.saveAll(landmarks);

        return landmarks;
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

//        landmarkRepository.save(landmark);
        return landmark;
    }

    // api 의 이미지를 db에 저장하기 위한 로직
    private LandmarkImageEntity convertToImageEntity(LandmarkDto.LandmarkApiResponseDto dto, LandmarkEntity landmark) {
        LandmarkImageEntity imageEntity = new LandmarkImageEntity();
        imageEntity.setImagePath(dto.getImagePath());
        imageEntity.setImageName(dto.getTitle());
        imageEntity.setLandmark(landmark);
        return imageEntity;
    }

    // 세부 여행지 불러오기(dto 변환)
    private LandmarkDto.LandmarkApiDetailResponseDto getLandmarksByContentTypeId(String contentId)
        throws URISyntaxException, JsonProcessingException {

        // API 에서 데이터 가져옴
        return convertToDetailLandmarkDto(tourApiTool.getLandmarkData(contentId)); // 필요한 파라미터 제공
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

}
