package com.wiztrip.controller;

import com.wiztrip.dto.TripDto;
import com.wiztrip.service.TripService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("trips")
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripDto.TripResponseDto> createTrip(@RequestBody TripDto.TripPostDto tripPostDto) {
        return ResponseEntity.ok().body(tripService.createTrip(tripPostDto));
    }

    @GetMapping
    public ResponseEntity<TripDto.TripResponseDto> getTrip(@RequestParam @NotNull @Min(1) Long tripId) {
        return ResponseEntity.ok().body(tripService.getTrip(tripId));
    }

    // todo: 잘 작동하는지 확인 필요!!!!
    @PatchMapping
    public ResponseEntity<TripDto.TripResponseDto> updateTrip(@RequestBody TripDto.TripPatchDto tripPatchDto) {
        return ResponseEntity.ok().body(tripService.updateTrip(tripPatchDto));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTrip(@RequestParam @NotNull @Min(1) Long tripId) {
        return ResponseEntity.ok().body(tripService.deleteTrip(tripId));
    }




}
