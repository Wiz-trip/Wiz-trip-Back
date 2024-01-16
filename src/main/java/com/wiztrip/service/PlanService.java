package com.wiztrip.service;

import com.wiztrip.domain.PlanEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.PlanDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.mapstruct.PlanMapper;
import com.wiztrip.repository.PlanRepository;
import com.wiztrip.repository.TripUserRepository;
import com.wiztrip.tool.redis.RedisTool;
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

    private final RedisTool redisTool;

    @Transactional
    public PlanDto.PlanResponseDto createPlan(UserEntity user, Long tripId, PlanDto.PlanPostDto planPostDto) {
        checkValidByUserIdAndTripId(user.getId(), tripId);
        return planMapper.toResponseDto(planRepository.save(planMapper.toEntity(planPostDto, tripId, user)));
    }

    public PlanDto.PlanResponseDto getPlan(UserEntity user, Long tripId, Long planId) {
        checkValidByUserIdAndTripId(user.getId(), tripId);
        PlanEntity plan = planRepository.findById(planId).orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND));
        checkValidByPlanAndTripId(plan, tripId);
        return planMapper.toResponseDto(plan);
    }

    public ListDto<PlanDto.PlanResponseDto> getAllPlanByTripId(UserEntity user, Long tripId) {
        checkValidByUserIdAndTripId(user.getId(), tripId);
        return new ListDto<>(planRepository.findAllByTripId(tripId).stream().map(o -> {
            checkValidByPlanAndTripId(o, tripId);
            return planMapper.toResponseDto(o);
        }).toList());
    }

    public Page<Long> getPlanIdPageByTripId(UserEntity user, Long tripId, Pageable pageable) {
        checkValidByUserIdAndTripId(user.getId(), tripId);
        return planRepository.findAllIdByTripId(tripId, pageable);
    }

    public Page<PlanDto.PlanResponseDto> getAllPlanPageByTripId(UserEntity user, Long tripId, Pageable pageable) {
        checkValidByUserIdAndTripId(user.getId(), tripId);
        return planRepository.findAllByTripId(tripId, pageable).map(planMapper::toResponseDto);
    }


    @Transactional
    public PlanDto.PlanResponseDto updatePlan(UserEntity user, Long tripId, PlanDto.PlanPatchDto planPatchDto) {
        checkValidByUserIdAndTripId(user.getId(), tripId);
        if(isLocked(planPatchDto.getPlanId())) throw new CustomException(ErrorCode.PLAN_IS_LOCKED);
        PlanEntity plan = planRepository.findById(planPatchDto.getPlanId()).orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND));
        checkValidByPlanAndTripId(plan, tripId);
        planMapper.updateFromPatchDto(planPatchDto, plan); //dirty checking을 활용한 update
        return planMapper.toResponseDto(planRepository.findById(planPatchDto.getPlanId()).orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND)));
    }

    @Transactional
    public String deletePlan(UserEntity user, Long tripId, Long planId) {
        checkValidByUserIdAndTripId(user.getId(), tripId);
        checkValidByPlanAndTripId(planRepository.findById(planId).orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND)), tripId);
        planRepository.deleteById(planId);
        return "planId: " + planId + " 삭제 완료";
    }

    private void checkValidByPlanAndTripId(PlanEntity plan, Long tripId) { //plan이 trip에 속하는지 확인
        if (!plan.getTrip().getId().equals(tripId)) throw new CustomException(ErrorCode.NOT_IN_TRIP_PLAN);
    }

    private void checkValidByUserIdAndTripId(Long userId, Long tripId) { //user가 trip에 속하는지 확인
        if (!tripUserRepository.existsByUserIdAndTripId(userId, tripId))
            throw new CustomException(ErrorCode.FORBIDDEN_TRIP_USER);
    }

    /**
     * 동시편집을 위한 method
     * lock, unlock을 통해 서로 다른 사용자가 같은 Plan에 동시에 수정하지 못하도록 함
     */
    private static final String LOCK_PREFIX = "planId:";
    public String lockPlan(Long planId) {
        if (redisTool.checkExistsValue(String.valueOf(planId))) {
            redisTool.setValues(LOCK_PREFIX +planId, "true");
        }
        return "lock success";
    }

    public String unlockPlan(Long planId) {
        if (redisTool.checkExistsValue(LOCK_PREFIX +planId))
            redisTool.setValues(LOCK_PREFIX +planId,"false");
        return "unlock success";
    }

    public boolean isLocked(Long planId) {
        return redisTool.checkExistsValue(LOCK_PREFIX + planId) &&
            redisTool.getValues(LOCK_PREFIX + planId).equals("true");
    }
}
