package com.wiztrip.mapstruct;

import com.wiztrip.domain.PlanEntity;
import com.wiztrip.domain.TripEntity;
import com.wiztrip.dto.PlanDto;
import com.wiztrip.repository.PlanRepository;
import com.wiztrip.repository.TripRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring", // 빌드 시 구현체 만들고 빈으로 등록
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, // 생성자 주입 전략
        unmappedTargetPolicy = ReportingPolicy.ERROR // 일치하지 않는 필드가 있으면 빌드 시 에러
)
public abstract class PlanMapper {

    @Autowired
    PlanRepository planRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    TripMapper tripMapper;

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "trip", source = "tripId", qualifiedByName = "tripIdToTripEntity")

    })
    public abstract PlanEntity toEntity(PlanDto.PlanPostDto planPostDto);

    @Mappings({
            @Mapping(target = "planId", source = "id"),
            @Mapping(target = "userId", expression = "java(plan.getUser().getId())"),
            @Mapping(target = "tripId", expression = "java(plan.getTrip().getId())")
    })
    public abstract PlanDto.PlanResponseDto toResponseDto(PlanEntity plan);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user",ignore = true),
            @Mapping(target = "trip", ignore = true)
    })
    public abstract void updateFromPatchDto(PlanDto.PlanPatchDto planPatchDto, @MappingTarget PlanEntity plan);

    @Named("tripIdToTripEntity")
    TripEntity tripIdToTripEntity(Long tripId) {
        return tripRepository.findById(tripId).orElseThrow();
    }
}
