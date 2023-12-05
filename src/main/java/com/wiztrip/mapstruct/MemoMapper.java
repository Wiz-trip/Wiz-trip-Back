package com.wiztrip.mapstruct;

import com.wiztrip.domain.MemoEntity;
import com.wiztrip.domain.TripEntity;
import com.wiztrip.dto.MemoDto;
import com.wiztrip.repository.MemoRepository;
import com.wiztrip.repository.TripRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring", // 빌드 시 구현체 만들고 빈으로 등록
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, // 생성자 주입 전략
        unmappedTargetPolicy = ReportingPolicy.ERROR // 일치하지 않는 필드가 있으면 빌드 시 에러
)

// @Autowired를 위해 interface 대신 abstract class 사용
public abstract class MemoMapper {

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    TripRepository tripRepository;

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "trip", source = "tripId", qualifiedByName = "tripIdToTripEntity")
    })
    public abstract MemoEntity toEntity(MemoDto.MemoPostDto memoPostDto, Long tripId);

    @Mappings({
            @Mapping(target = "memoId", source = "id"),
            @Mapping(target = "tripId", expression = "java(memo.getTrip().getId())")
    })
    public abstract MemoDto.MemoResponseDto toResponseDto(MemoEntity memo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "trip", ignore = true)
    })
    public abstract void updateFromPatchDto(MemoDto.MemoPatchDto memoPatchDto, @MappingTarget MemoEntity memo);

    @Named("tripIdToTripEntity")
    TripEntity tripIdToTripEntity(Long tripId) {
        return tripRepository.findById(tripId).orElseThrow();
    }
}
