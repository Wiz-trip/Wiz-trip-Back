package com.wiztrip.mapstruct;

import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.domain.LandmarkImageEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.repository.LandmarkRepository;
import com.wiztrip.tool.file.Base64Dto;
import com.wiztrip.tool.file.Base64Tool;
import com.wiztrip.tool.file.FtpTool;
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

    @Autowired
    FtpTool ftpTool;

    @Autowired
    Base64Tool base64Tool;

    // LandmarkEntity -> LandmarkAllResponseDto 변환
    @Mappings({
            @Mapping(target = "landmarkId", source = "id"),
            @Mapping(target = "imageList", expression = "java(toImageEntity(entity.getImageList()))")
    })
    public abstract LandmarkDto.LandmarkAllResponseDto entityToAllResponseDto(LandmarkEntity entity);

    // LandmarkEntity -> LandmarkDetailResponseDto 변환
    @Mappings({
            @Mapping(target = "landmarkId", source = "id"),
            @Mapping(target = "content",source = "content"),
            @Mapping(target = "imageList", expression = "java(toImageEntity(entity.getImageList()))")
    })
    public abstract LandmarkDto.LandmarkDetailResponseDto entityToDetailResponseDto(LandmarkEntity entity);

    /*
    // Image 객체 리스트 -> String 리스트 변환
    @Named("imagesToStrings")
    List<String> imagesToStrings(List<Image> images) {
        // Image 객체를 String으로 변환하는 로직 구현 (예: URL 추출 등)
        return images.stream()
                .map(Image::toString)
                .collect(Collectors.toList());
    }
    */

    @Named("toImageEntity")
    public List<Base64Dto> toImageEntity(List<LandmarkImageEntity> fileList) {
        return fileList.stream()
                .map(image -> base64Tool.base64StringToDto(image.getImageName(), ftpTool.downloadFileAndConvertToBase64String(image.getImageName())))
                .collect(Collectors.toList());
    }
}