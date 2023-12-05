package com.wiztrip.mapstruct;

import com.wiztrip.domain.ReviewEntity;
import com.wiztrip.domain.TripEntity;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.repository.ReviewRepository;
import com.wiztrip.repository.TripRepository;
import com.wiztrip.repository.UserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    UserRepository userRepository;

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target ="user", ignore = true),
            @Mapping(target = "trip", source = "tripId", qualifiedByName = "tripIdToTripEntity"),
            @Mapping(target = "imageList", source = "reviewPostDto.imageList")
    })
    public abstract ReviewEntity toEntity(ReviewDto.ReviewPostDto reviewPostDto, Long tripId);

    @Mappings({
            @Mapping(target = "reviewId", source = "id"),
            @Mapping(target = "userId", expression = "java(review.getUser().getId())"),
            @Mapping(target = "tripId", expression = "java(review.getTrip().getId())"),
            @Mapping(target = "imageList", expression = "java(review.getImageList())")
    })
    public abstract ReviewDto.ReviewResponseDto toResponseDto(ReviewEntity review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user",ignore = true),
            @Mapping(target = "trip", ignore = true),
            @Mapping(target = "imageList", source = "reviewPatchDto.imageList")
    })
    public abstract void updateFromPatchDto(ReviewDto.ReviewPatchDto reviewPatchDto, @MappingTarget ReviewEntity review);

    @Named("tripIdToTripEntity")
    TripEntity tripIdToTripEntity(Long tripId) {
        return tripRepository.findById(tripId).orElseThrow();
    }
}
