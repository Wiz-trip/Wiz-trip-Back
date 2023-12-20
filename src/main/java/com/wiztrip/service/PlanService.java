package com.wiztrip.service;

import com.wiztrip.domain.PlanEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.PlanDto;
import com.wiztrip.mapstruct.PlanMapper;
import com.wiztrip.repository.PlanRepository;
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

    @Transactional
    public PlanDto.PlanResponseDto createPlan(UserEntity user, Long tripId, PlanDto.PlanPostDto planPostDto) {
        return planMapper.toResponseDto(planRepository.save(planMapper.toEntity(planPostDto, tripId, user)));
    }

    public PlanDto.PlanResponseDto getPlan(Long tripId, Long planId) {
        PlanEntity plan = planRepository.findById(planId).orElseThrow();
        checkValid(plan,tripId);
        return planMapper.toResponseDto(plan);
    }

    public ListDto<PlanDto.PlanResponseDto> getAllPlanByTripId(Long tripId) {
        return new ListDto<>(planRepository.findAllByTripId(tripId).stream().map(o -> {
            checkValid(o, tripId);
            return planMapper.toResponseDto(o);
        }).toList());
    }

    public Page<Long> getPlanIdPageByTripId(Long tripId, Pageable pageable) {
        return planRepository.findAllIdByTripId(tripId, pageable);
    }

    public Page<PlanDto.PlanResponseDto> getAllPlanPageByTripId(Long tripId, Pageable pageable) {
        return planRepository.findAllByTripId(tripId, pageable).map(planMapper::toResponseDto);
    }


    @Transactional
    public PlanDto.PlanResponseDto updatePlan(Long tripId, PlanDto.PlanPatchDto planPatchDto) {
        PlanEntity plan = planRepository.findById(planPatchDto.getPlanId()).orElseThrow();
        checkValid(plan,tripId);
        planMapper.updateFromPatchDto(planPatchDto, plan); //dirty checking을 활용한 update
        return planMapper.toResponseDto(planRepository.findById(planPatchDto.getPlanId()).orElseThrow());
    }

    @Transactional
    public String deletePlan(Long tripId, Long planId) {
        if (!planRepository.existsById(planId)) return "planId: " + planId + "  존재하지 않는 plan입니다.";
        checkValid(planRepository.findById(planId).orElseThrow(),tripId);
        planRepository.deleteById(planId);
        return "planId: " + planId + " 삭제 완료";
    }

    private static void checkValid(PlanEntity plan, Long tripId) {
        if (!plan.getTrip().getId().equals(tripId)) throw new RuntimeException("Permission denied");
    }
}
