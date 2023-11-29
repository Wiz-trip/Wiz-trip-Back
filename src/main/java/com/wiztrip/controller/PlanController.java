package com.wiztrip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips/{trip_id}/plans")
public class PlanController {

    @PostMapping
    public void createPlan(@PathVariable("trip_id") Long tripId) {

    }

    @GetMapping
    public void getPlan(@PathVariable("trip_id") Long tripId) {

    }

    @GetMapping(headers = "range=all")
    public void getAllPlan(@PathVariable("trip_id") Long tripId) {

    }

    @PatchMapping
    public void updatePlan(@PathVariable("trip_id") Long tripId) {

    }

    @DeleteMapping
    public void deletePlan(@PathVariable("trip_id") Long tripId) {

    }

}
