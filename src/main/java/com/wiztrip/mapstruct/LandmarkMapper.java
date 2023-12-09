package com.wiztrip.mapstruct;


import com.wiztrip.constant.Image;
import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.domain.MemoEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.dto.MemoDto;
import com.wiztrip.repository.LandmarkRepository;
import com.wiztrip.repository.LikeRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring", // 빌드 시 구현체 만들고 빈으로 등록
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, // 생성자 주입 전략
        unmappedTargetPolicy = ReportingPolicy.ERROR // 일치하지 않는 필드가 있으면 빌드 시 에러
)

public abstract class LandmarkMapper {

    @Autowired
    LandmarkRepository landmarkRepository;


    // LandmarkDto -> LandmarkEntity  변환
    // id 뺴고 매핑 진행
    @Mapping(target = "id",ignore = true)
    public abstract LandmarkEntity dtoToEntity(LandmarkDto landmarkDto);

    // LandmarkEntity -> LandmarkDto 변환
    public abstract LandmarkDto entityToDto(LandmarkEntity entity);

    // LandmarkEntity -> LandmarkAllResponseDto  매핑 (Image -> String 변환)
    @Mapping(target = "imageList", source = "imageList", qualifiedByName = "imagesToStrings")
    public abstract LandmarkDto.LandmarkAllResponseDto entityToLandmarkAllResponseDto(LandmarkEntity entity);


    // LandmarkEntity -> LandmarkDetailResponseDto  매핑 (Image -> String 변환)
    @Mapping(target = "imageList", source = "imageList", qualifiedByName = "imagesToStrings")
    public abstract LandmarkDto.LandmarkDetailResponseDto entityToLandmarkDetailResponseDto(LandmarkEntity entity);

    // Image 객체 리스트 ->  String 리스트  변환
    @Named("imagesToStrings")
    List<String> imagesToStrings(List<Image> images) {  // Mapstruct 내부적으로 작동되는 메서드
        // Image 객체를 String으로 변환하는 로직 구현
        return images.stream().map(Image::toString).collect(Collectors.toList());
    }



}
