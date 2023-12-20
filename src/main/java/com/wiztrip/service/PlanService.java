package com.wiztrip.service;

import com.wiztrip.domain.PlanEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.PlanDto;
import com.wiztrip.mapstruct.PlanMapper;
import com.wiztrip.repository.PlanRepository;
import com.wiztrip.repository.TripUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PlanService {

    private final PlanMapper planMapper;

    private final PlanRepository planRepository;

    private final TripUserRepository tripUserRepository;

    @Transactional
    public PlanDto.PlanResponseDto createPlan(UserEntity user, Long tripId, PlanDto.PlanPostDto planPostDto) {
        checkValidByUserIdAndTripId(user.getId(), tripId);
        return planMapper.toResponseDto(planRepository.save(planMapper.toEntity(planPostDto, tripId, user)));
    }

    public PlanDto.PlanResponseDto getPlan(UserEntity user, Long tripId, Long planId) {
        checkValidByUserIdAndTripId(user.getId(),tripId);
        PlanEntity plan = planRepository.findById(planId).orElseThrow();
        checkValidByPlanAndTripId(plan, tripId);
        return planMapper.toResponseDto(plan);
    }

    public ListDto<PlanDto.PlanResponseDto> getAllPlanByTripId(UserEntity user, Long tripId) {
        checkValidByUserIdAndTripId(user.getId(),tripId);
        return new ListDto<>(planRepository.findAllByTripId(tripId).stream().map(o -> {
            checkValidByPlanAndTripId(o, tripId);
            return planMapper.toResponseDto(o);
        }).toList());
    }

    public Page<Long> getPlanIdPageByTripId(UserEntity user, Long tripId, Pageable pageable) {
        checkValidByUserIdAndTripId(user.getId(),tripId);
        return planRepository.findAllIdByTripId(tripId, pageable);
    }

    public Page<PlanDto.PlanResponseDto> getAllPlanPageByTripId(UserEntity user, Long tripId, Pageable pageable) {
        checkValidByUserIdAndTripId(user.getId(),tripId);
        return planRepository.findAllByTripId(tripId, pageable).map(planMapper::toResponseDto);
    }


    @Transactional
    public PlanDto.PlanResponseDto updatePlan(UserEntity user, Long tripId, PlanDto.PlanPatchDto planPatchDto) {
        checkValidByUserIdAndTripId(user.getId(),tripId);
        PlanEntity plan = planRepository.findById(planPatchDto.getPlanId()).orElseThrow();
        checkValidByPlanAndTripId(plan, tripId);
        planMapper.updateFromPatchDto(planPatchDto, plan); //dirty checking을 활용한 update
        return planMapper.toResponseDto(planRepository.findById(planPatchDto.getPlanId()).orElseThrow());
    }

    @Transactional
    public String deletePlan(UserEntity user, Long tripId, Long planId) {
        checkValidByUserIdAndTripId(user.getId(),tripId);
        if (!planRepository.existsById(planId)) return "planId: " + planId + "  존재하지 않는 plan입니다.";
        checkValidByPlanAndTripId(planRepository.findById(planId).orElseThrow(), tripId);
        planRepository.deleteById(planId);
        return "planId: " + planId + " 삭제 완료";
    }

    private void checkValidByPlanAndTripId(PlanEntity plan, Long tripId) { //plan이 trip에 속하는지 확인
        if (!plan.getTrip().getId().equals(tripId)) throw new RuntimeException("Permission denied");
    }

    private void checkValidByUserIdAndTripId(Long userId, Long tripId) { //user가 trip에 속하는지 확인
        if(!tripUserRepository.existsByUserIdAndTripId(userId,tripId)) throw new RuntimeException("Permission denied");
    }

}
