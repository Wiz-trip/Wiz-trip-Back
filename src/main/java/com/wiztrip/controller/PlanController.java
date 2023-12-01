package com.wiztrip.controller;

import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.PlanDto;
import com.wiztrip.service.PlanService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips/{trip_id}/plans")
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<PlanDto.PlanResponseDto> createPlan(@PathVariable("trip_id") @NotNull Long tripId, @RequestBody PlanDto.PlanPostDto planPostDto) {
        return ResponseEntity.ok().body(planService.createPlan(tripId, planPostDto));
    }

    @GetMapping
    public ResponseEntity<PlanDto.PlanResponseDto> getPlan(@PathVariable("trip_id") Long tripId, @RequestParam @NotNull @Min(1) Long planId) {
        return ResponseEntity.ok().body(planService.getPlan(tripId, planId));
    }

    @GetMapping(headers = "range=all")
    public ResponseEntity<ListDto> getAllPlan(@PathVariable("trip_id") Long tripId) {
        return ResponseEntity.ok().body(planService.getAllPlan(tripId));
    }

    @PatchMapping
    public ResponseEntity<PlanDto.PlanResponseDto> updatePlan(@PathVariable("trip_id") Long tripId, @RequestBody PlanDto.PlanPatchDto planPatchDto) {
        return ResponseEntity.ok().body(planService.updatePlan(tripId, planPatchDto));

    }

    @DeleteMapping
    public ResponseEntity<String> deletePlan(@PathVariable("trip_id") Long tripId, @RequestParam @NotNull @Min(1) Long planId) {
        return ResponseEntity.ok().body(planService.deletePlan(tripId,planId));
    }

}
