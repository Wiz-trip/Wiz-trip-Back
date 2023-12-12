package com.wiztrip.mapstruct;


import com.wiztrip.constant.Image;
import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.repository.LandmarkRepository;
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

    // LandmarkEntity -> LandmarkAllResponseDto 변환
    @Mappings({
            @Mapping(target = "landmarkId", source = "id"),
            @Mapping(target = "imageList", source = "imageList", qualifiedByName = "imagesToStrings")
    })
    public abstract LandmarkDto.LandmarkAllResponseDto entityToAllResponseDto(LandmarkEntity entity);

    // LandmarkEntity -> LandmarkDetailResponseDto 변환
    @Mappings({
            @Mapping(target = "landmarkId", source = "id"),
            @Mapping(target = "content",source = "content"),
            @Mapping(target = "imageList", source = "imageList", qualifiedByName = "imagesToStrings")
    })
    public abstract LandmarkDto.LandmarkDetailResponseDto entityToDetailResponseDto(LandmarkEntity entity);

    // Image 객체 리스트 -> String 리스트 변환
    @Named("imagesToStrings")
    List<String> imagesToStrings(List<Image> images) {
        // Image 객체를 String으로 변환하는 로직 구현 (예: URL 추출 등)
        return images.stream()
                .map(Image::toString)
                .collect(Collectors.toList());
    }

}