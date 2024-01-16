package com.wiztrip.mapstruct;

import com.wiztrip.domain.ReviewEntity;
import com.wiztrip.domain.ReviewImageEntity;
import com.wiztrip.domain.TripEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.repository.ReviewImageRepository;
import com.wiztrip.repository.ReviewRepository;
import com.wiztrip.repository.TripRepository;
import com.wiztrip.tool.file.Base64Dto;
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

// @Autowired를 위해 interface 대신 abstract class 사용
public abstract class ReviewMapper {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewImageRepository reviewImageRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    FtpTool ftpTool;

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "createAt", ignore = true),
            @Mapping(target = "modifiedAt",ignore = true),
            @Mapping(target = "trip", source = "tripId", qualifiedByName = "tripIdToTripEntity"),
            @Mapping(target = "imageList", ignore = true)
    })
    public abstract ReviewEntity toEntity(UserEntity user, Long tripId, ReviewDto.ReviewPostDto reviewPostDto);

    @Mappings({
            @Mapping(target = "reviewId", source = "id"),
            @Mapping(target = "userId", expression = "java(review.getUser().getId())"),
            @Mapping(target = "tripId", expression = "java(review.getTrip().getId())"),
            @Mapping(target = "imageList", expression = "java(toImageEntity(review.getImageList()))")
    })
    public abstract ReviewDto.ReviewResponseDto toResponseDto(ReviewEntity review);

    @Mappings({
            @Mapping(target = "reviewId", source = "id"),
            @Mapping(target = "userId", expression = "java(review.getUser().getId())"),
            @Mapping(target = "tripId", expression = "java(review.getTrip().getId())"),
            @Mapping(target = "destination", expression = "java(review.getTrip().getDestination())"),
            @Mapping(target = "startDate", expression = "java(review.getTrip().getStartDate())"),
            @Mapping(target = "finishDate", expression = "java(review.getTrip().getFinishDate())"),
            @Mapping(target = "imageList", expression = "java(toImageEntity(review.getImageList()))")
    })
    public abstract ReviewDto.MyReviewResponseDto toMyResponseDto(ReviewEntity review);

    @Mappings({
            @Mapping(target = "reviewNum", source = "reviewNum")
    })
    public abstract ReviewDto.MyReviewCountResponseDto toMyCountResponseDto(Integer reviewNum);

    @Mappings({
            @Mapping(target = "reviewNum", source = "reviewNum")
    })
    public abstract ReviewDto.ToReviewCountResponseDto toToCountResponseDto(Integer reviewNum);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user",ignore = true),
            @Mapping(target = "trip", ignore = true),
            @Mapping(target = "createAt", ignore = true),
            @Mapping(target = "modifiedAt",ignore = true),
            @Mapping(target = "imageList", source = "reviewPatchDto.fileNameList", qualifiedByName = "getImageEntity")
    })
    public abstract void updateFromPatchDto(ReviewDto.ReviewPatchDto reviewPatchDto, @MappingTarget ReviewEntity review);

    @Named("tripIdToTripEntity")
    TripEntity tripIdToTripEntity(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND));
    }

    @Named("getImageEntity")
    public List<ReviewImageEntity> getImageEntity(List<String> fileNameList) {
        return fileNameList.stream()
                .map(fileName ->
                    reviewImageRepository.findByImageName(fileName)
                            .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND))
                )
                .collect(Collectors.toList());
    }

    @Named("toImageEntity")
    public List<Base64Dto> toImageEntity(List<ReviewImageEntity> fileList) {
        return fileList.stream()
                .map(image -> ftpTool.downloadFileAndConvertToBase64Dto(image.getImageName()))
                .collect(Collectors.toList());
    }
}
