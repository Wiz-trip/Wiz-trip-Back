package com.wiztrip.controller;

import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.PlanDto;
import com.wiztrip.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Plan(세부 계획)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/trips/{trip_id}/plans")
public class PlanController {

    private final PlanService planService;

    @Operation(summary = "Plan 생성", description = "trip id와 PlanPostDto를 사용해 Plan 생성. PlanPostDto에  JWT Token 입력 필수!!")
    @PostMapping
    public ResponseEntity<PlanDto.PlanResponseDto> createPlan(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Schema(description = "trip id", example = "1")
            @PathVariable("trip_id") @NotNull Long tripId,
            @RequestBody PlanDto.PlanPostDto planPostDto) {
        return ResponseEntity.ok().body(planService.createPlan(principalDetails.getUser(), tripId, planPostDto));
    }

    @Operation(summary = "Plan 조회", description = "trip id와 plan id를 사용해 Plan 조회")
    @GetMapping
    public ResponseEntity<PlanDto.PlanResponseDto> getPlan(
            @Schema(description = "trip id", example = "1")
            @PathVariable("trip_id") Long tripId,
            @Schema(description = "plan id", example = "1")
            @RequestParam @NotNull @Min(1) Long planId) {
        return ResponseEntity.ok().body(planService.getPlan(tripId, planId));
    }

    @Operation(summary = "Trip의 모든 Plan 조회", description = "trip id와 plan id를 사용해 Trip의 모든 Plan 조회")
    @GetMapping("/all")
    public ResponseEntity<ListDto<PlanDto.PlanResponseDto>> getAllPlan(
            @Schema(description = "trip id", example = "1")
            @PathVariable("trip_id") Long tripId) {
        return ResponseEntity.ok().body(planService.getAllPlanByTripId(tripId));
    }

    @Operation(summary = "Trip의 모든 Plan id Page 조회", description = "trip id와 plan id를 사용해 Trip의 모든 Plan id Page 조회")
    @GetMapping("/page")
    public ResponseEntity<Page<Long>> getPlanIdPageByTripId(
            @Schema(description = "trip id", example = "1")
            @PathVariable("trip_id") Long tripId,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "startTime") String... sortBy
    ) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sortDirection, sortBy);
        return ResponseEntity.ok().body(planService.getPlanIdPageByTripId(tripId, pageRequest));
    }

    @Operation(summary = "Trip의 모든 Plan Page 조회", description = "trip id와 plan id를 사용해 Trip의 모든 Plan Page 조회")
    @GetMapping("/with-details/page")
    public ResponseEntity<Page<PlanDto.PlanResponseDto>> getPlanPageByTripId(
            @Schema(description = "trip id", example = "1")
            @PathVariable("trip_id") Long tripId,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "startTime") String... sortBy
    ) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sortDirection, sortBy);
        return ResponseEntity.ok().body(planService.getAllPlanPageByTripId(tripId, pageRequest));
    }


    @Operation(summary = "Plan 수정", description = "trip id와 PlanPatchDto를 사용해 Plan 수정. PlanPatchDto에서 수정할 attribute만 담아서 보내야 함!!")
    @PatchMapping
    public ResponseEntity<PlanDto.PlanResponseDto> updatePlan(
            @Schema(description = "trip id", example = "1")
            @PathVariable("trip_id") Long tripId,
            @RequestBody PlanDto.PlanPatchDto planPatchDto) {
        return ResponseEntity.ok().body(planService.updatePlan(tripId, planPatchDto));

    }

    @Operation(summary = "Plan 삭제", description = "trip id와 plan id를 사용해 Plan 삭제")
    @DeleteMapping
    public ResponseEntity<String> deletePlan(
            @Schema(description = "trip id", example = "1")
            @PathVariable("trip_id") Long tripId,
            @Schema(description = "plan id", example = "1")
            @RequestParam @NotNull @Min(1) Long planId) {
        return ResponseEntity.ok().body(planService.deletePlan(tripId, planId));
    }

}
