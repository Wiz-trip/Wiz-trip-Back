package com.wiztrip.mapstruct;

import com.wiztrip.domain.PlanEntity;
import com.wiztrip.domain.TripEntity;
import com.wiztrip.domain.TripUserEntity;
import com.wiztrip.dto.TripDto;
import com.wiztrip.repository.PlanRepository;
import com.wiztrip.repository.TripRepository;
import com.wiztrip.repository.UserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring", // 빌드 시 구현체 만들고 빈으로 등록
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, // 생성자 주입 전략
        unmappedTargetPolicy = ReportingPolicy.ERROR // 일치하지 않는 필드가 있으면 빌드 시 에러
)
public abstract class TripMapper {
    @Autowired
    TripRepository tripRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    UserRepository userRepository;

    public TripEntity toEntity(TripDto.TripPostDto tripPostDto) {
        TripEntity trip = _toEntity(tripPostDto);
        if(trip.getTripUserEntityList()==null) throw new RuntimeException("Trip에 속한 User가 없습니다.");
        trip.getPlanEntityList().forEach(o->o.setTrip(trip)); //연관관계 처리
        return trip;
    }

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "planEntityList", ignore = true),
            @Mapping(target = "tripUserEntityList", source = "userIdList", qualifiedByName = "userIdListToTripUserEntityList"),
    })
    abstract TripEntity _toEntity(TripDto.TripPostDto tripPostDto);

    @Mappings({
            @Mapping(target = "tripId", source = "id"),
            @Mapping(target = "userIdList", expression = "java(trip.getTripUserEntityList().stream().map(o->o.getUser().getId()).toList())"),
            @Mapping(target = "planIdList", expression = "java(trip.getPlanEntityList().stream().map(o->o.getId()).toList())")
    })
    public abstract TripDto.TripResponseDto toResponseDto(TripEntity trip);

    public void updateFromPatchDto(TripDto.TripPatchDto tripPatchDto, TripEntity trip) {
        _updateFromPatchDto(tripPatchDto, trip);

        //만약 trip에 참여한 user가 변경되었다면
        if(!tripPatchDto.getUserIdList().isEmpty())  trip.getTripUserEntityList().forEach(o->o.setTrip(trip));
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "tripUserEntityList", source = "userIdList", qualifiedByName = "userIdListToTripUserEntityList"),
            @Mapping(target = "planEntityList", source = "planIdList", qualifiedByName = "planIdListToPlanEntityList")
    })
    abstract void _updateFromPatchDto(TripDto.TripPatchDto tripPatchDto, @MappingTarget TripEntity trip);

    @Named("userIdListToTripUserEntityList")
    List<TripUserEntity> userIdListToTripUserEntityList(List<Long> userIdList) {
        if (userIdList.isEmpty()) return null; //update시 dto의 list가 비어있으면 null을 리턴해야 기존 값 유지됨

        return userIdList.stream().map(o -> new TripUserEntity(userRepository.findById(o).orElseThrow())).toList();
    }

    @Named("planIdListToPlanEntityList")
    List<PlanEntity> planIdListToPlanEntityList(List<Long> planIdList) {
        if (planIdList.isEmpty()) return null; //update시 dto의 list가 비어있으면 null을 리턴해야 기존 값 유지됨

        return planIdList.stream().map(o -> planRepository.findById(o).orElseThrow()).toList();
    }
}
