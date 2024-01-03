package com.wiztrip.mapstruct;

import com.wiztrip.domain.ReviewEntity;
import com.wiztrip.domain.ReviewImageEntity;
import com.wiztrip.domain.TripEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.repository.ReviewRepository;
import com.wiztrip.repository.TripRepository;
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
    TripRepository tripRepository;

    public ReviewEntity toEntity(UserEntity user, Long tripId, ReviewDto.ReviewPostDto reviewPostDto) {
        ReviewEntity review = _toEntity(user, tripId, reviewPostDto);
        review.getImageList().forEach(o->o.setReview(review));
        return review;
    }

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "createAt", ignore = true),
            @Mapping(target = "modifiedAt",ignore = true),
            @Mapping(target = "trip", source = "tripId", qualifiedByName = "tripIdToTripEntity"),
            @Mapping(target = "imageList", source = "reviewPostDto.imageList", qualifiedByName = "getImageEntity")
    })
    abstract ReviewEntity _toEntity(UserEntity user, Long tripId, ReviewDto.ReviewPostDto reviewPostDto);

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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user",ignore = true),
            @Mapping(target = "trip", ignore = true),
            @Mapping(target = "createAt", ignore = true),
            @Mapping(target = "modifiedAt",ignore = true),
            @Mapping(target = "imageList", source = "reviewPatchDto.imageList", qualifiedByName = "getImageEntity")
    })
    public abstract void updateFromPatchDto(ReviewDto.ReviewPatchDto reviewPatchDto, @MappingTarget ReviewEntity review);

    @Named("tripIdToTripEntity")
    TripEntity tripIdToTripEntity(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND));
    }

    @Named("getImageEntity")
    public List<ReviewImageEntity> getImageEntity(List<ReviewDto.ReviewImageDto> imageList) {
        return imageList.stream()
                .map(image -> {
                    ReviewImageEntity reviewImageEntity = new ReviewImageEntity();
                    reviewImageEntity.setImagePath(image.getImagePath());
                    reviewImageEntity.setImageName(image.getImageName());
                    return reviewImageEntity;
                })
                .collect(Collectors.toList());
    }

    @Named("toImageEntity")
    public List<ReviewDto.ReviewImageDto> toImageEntity(List<ReviewImageEntity> imageList) {
        return imageList.stream()
                .map(image -> ReviewDto.ReviewImageDto.builder()
                        .imagePath(image.getImagePath())
                        .imageName(image.getImageName())
                        .build())
                .collect(Collectors.toList());
    }
}
