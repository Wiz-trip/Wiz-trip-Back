package com.wiztrip.mapstruct;

import com.wiztrip.domain.PlanEntity;
import com.wiztrip.domain.TripEntity;
import com.wiztrip.domain.TripUserEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.TripDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.repository.PlanRepository;
import com.wiztrip.repository.TripRepository;
import com.wiztrip.repository.UserRepository;
import com.wiztrip.tool.redis.RedisTool;
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

    @Autowired
    RedisTool redisTool;

    public TripEntity toEntity(UserEntity user, TripDto.TripPostDto tripPostDto) {
        TripEntity trip = _toEntity(user, tripPostDto);
        if(trip.getTripUserEntityList()==null) throw new CustomException(ErrorCode.WRONG_DTO);
        trip.getTripUserEntityList().forEach(o->o.setTrip(trip));
        trip.getPlanEntityList().forEach(o->o.setTrip(trip)); //연관관계 처리
        return trip;
    }

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "owner", source = "owner"),
            @Mapping(target = "planEntityList", ignore = true),
            @Mapping(target = "memoEntityList", ignore = true),//todo: 나중에 수정해야함!!!!
            @Mapping(target = "reviewEntityList", ignore = true),
            @Mapping(target = "tripUserEntityList", source = "tripPostDto.userIdList", qualifiedByName = "userIdListToTripUserEntityList"),
            @Mapping(target = "tripUrlEntityList", ignore = true),
            @Mapping(target = "finished", ignore = true)
    })
    abstract TripEntity _toEntity(UserEntity owner, TripDto.TripPostDto tripPostDto);

    @Mappings({
            @Mapping(target = "tripId", source = "id"),
            @Mapping(target = "ownerId", expression = "java(trip.getOwner().getId())"),
            @Mapping(target = "userIdList", expression = "java(trip.getTripUserEntityList().stream().map(o->o.getUser().getId()).toList())"),
            @Mapping(target = "planIdList", expression = "java(trip.getPlanEntityList().stream().map(o->o.getId()).toList())"),
            @Mapping(target = "finished", expression = "java(trip.isFinished())")
    })
    public abstract TripDto.TripResponseDto toResponseDto(TripEntity trip);

    @Mappings({
            @Mapping(target = "tripId", source = "id"),
            @Mapping(target = "userIdList", expression = "java(trip.getTripUserEntityList().stream().map(o->o.getUser().getId()).toList())")
    })
    public abstract TripDto.TripUserResponseDto toTripUserResponseDto(TripEntity trip);

    @Mappings({
            @Mapping(target = "url", expression = "java(redisTool.getValues(trip.getId().toString()))")
    })
    public abstract TripDto.TripUrlResponseDto toUrlResponseDto(TripEntity trip);

    @Mappings({
            @Mapping(target = "tripId", source = "id")
    })
    public abstract TripDto.TripIdResponseDto toTripIdResponseDto(TripEntity trip);

    @Mappings({
            @Mapping(target = "tripNum", source = "tripNum")
    })
    public abstract TripDto.MyTripCountResponseDto toMyCountResponseDto(Integer tripNum);

    public void updateFromPatchDto(TripDto.TripPatchDto tripPatchDto, TripEntity trip) {
        _updateFromPatchDto(tripPatchDto, trip);

        //만약 trip에 참여한 user가 변경되었다면
        if(!tripPatchDto.getUserIdList().isEmpty())  trip.getTripUserEntityList().forEach(o->o.setTrip(trip));
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "owner", ignore = true),
            @Mapping(target = "planEntityList", source = "planIdList", qualifiedByName = "planIdListToPlanEntityList"),
            @Mapping(target = "memoEntityList", ignore = true), //todo: 나중에 수정해야함!!!!
            @Mapping(target = "reviewEntityList", ignore = true),
            @Mapping(target = "tripUserEntityList", source = "userIdList", qualifiedByName = "userIdListToTripUserEntityList"),
            @Mapping(target = "tripUrlEntityList", ignore = true),
            @Mapping(target = "finished", ignore = true)
    })
    abstract void _updateFromPatchDto(TripDto.TripPatchDto tripPatchDto, @MappingTarget TripEntity trip);

    @Named("userIdListToTripUserEntityList")
    List<TripUserEntity> userIdListToTripUserEntityList(List<Long> userIdList) {
        if (userIdList.isEmpty()) return null; //update시 dto의 list가 비어있으면 null을 리턴해야 기존 값 유지됨

        return userIdList.stream().map(o -> new TripUserEntity(userRepository.findById(o).orElseThrow(()->new CustomException(ErrorCode.UNAUTHORIZED_MEMBER)))).toList();
    }

    @Named("planIdListToPlanEntityList")
    List<PlanEntity> planIdListToPlanEntityList(List<Long> planIdList) {
        if (planIdList.isEmpty()) return null; //update시 dto의 list가 비어있으면 null을 리턴해야 기존 값 유지됨

        return planIdList.stream().map(o -> planRepository.findById(o).orElseThrow(()->new CustomException(ErrorCode.PLAN_NOT_FOUND))).toList();
    }
}
