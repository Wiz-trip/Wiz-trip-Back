package com.wiztrip.controller;

import com.wiztrip.dto.TripDto;
import com.wiztrip.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Trip")
@RestController
@RequiredArgsConstructor
@RequestMapping("trips")
public class TripController {

    private final TripService tripService;

    @Operation(summary = "Trip 생성", description = "TripPostDto를 사용해 Trip 생성. TripPostDto.userIdList에 자신의 id 꼭 포함해야함!!!")
    @PostMapping
    public ResponseEntity<TripDto.TripResponseDto> createTrip(@RequestBody TripDto.TripPostDto tripPostDto) {
        return ResponseEntity.ok().body(tripService.createTrip(tripPostDto));
    }

    @Operation(summary = "Trip 조회", description = "trip id를 통해 Trip 정보 조회")
    @GetMapping
    public ResponseEntity<TripDto.TripResponseDto> getTrip(
            @Schema(description = "trip id", example = "1")
            @RequestParam @NotNull @Min(1) Long tripId) {
        return ResponseEntity.ok().body(tripService.getTrip(tripId));
    }

    @Operation(summary = "Trip 수정", description = "TripPatchDto를 통해 Trip 수정. TripPatchDto.userIdList, TripPatchDto.planIdList를 조정해 참여중인 User, 포함된 Plan을 추가, 삭제 할 수 있음. 수정하고싶은 attribute만 포함해서 보내야함!! ")
    @PatchMapping
    public ResponseEntity<TripDto.TripResponseDto> updateTrip(@RequestBody TripDto.TripPatchDto tripPatchDto) {
        return ResponseEntity.ok().body(tripService.updateTrip(tripPatchDto));
    }

    @Operation(summary = "Trip 삭제", description = "trip id를 통해 trip 삭제")
    @DeleteMapping
    public ResponseEntity<String> deleteTrip(
            @Schema(description = "trip id",example = "1")
            @RequestParam @NotNull @Min(1) Long tripId) {
        return ResponseEntity.ok().body(tripService.deleteTrip(tripId));
    }




}
