package com.wiztrip.controller;

import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.dto.TripDto;
import com.wiztrip.service.TripService;
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

@Tag(name = "Trip")
@RestController
@RequiredArgsConstructor
@RequestMapping("trips")
public class TripController {

    private final TripService tripService;

    @Operation(summary = "Trip 생성", description = "TripPostDto를 사용해 Trip 생성. TripPostDto.userIdList에 자신의 id 꼭 포함해야함!!!")
    @PostMapping
    public ResponseEntity<TripDto.TripResponseDto> createTrip(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody TripDto.TripPostDto tripPostDto) {
        return ResponseEntity.ok().body(tripService.createTrip(principalDetails.getUser(), tripPostDto));
    }

    @Operation(summary = "Trip 조회", description = "trip id를 통해 Trip 정보 조회")
    @GetMapping
    public ResponseEntity<TripDto.TripResponseDto> getTrip(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Schema(description = "trip id", example = "1")
            @RequestParam @NotNull @Min(1) Long tripId) {
        return ResponseEntity.ok().body(tripService.getTrip(principalDetails.getUser(),tripId));
    }

    @Operation(summary = "User가 속한 Trip id 조회 - Page",description = "User가 속한 Trip의 id를 담은 Page 조회. JWT Token 필수!!!")
    @GetMapping("/page")
    public ResponseEntity<Page<Long>> getTripIdPageByUser(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "startDate") String... sortBy
    ) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sortDirection, sortBy);
        return ResponseEntity.ok().body(tripService.getTripIdPageByUser(principalDetails.getUser(), pageRequest));
    }

    @Operation(summary = "User가 속한 Trip detail 조회 - Page",description = "User가 속한 Trip의 Detail을 담은 Page 조회. JWT Token 필수!!!")
    @GetMapping("/with-details/page")
    public ResponseEntity<Page<TripDto.TripResponseDto>> getTripDetailsPageByUser(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "startDate") String... sortBy
    ) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sortDirection, sortBy);
        return ResponseEntity.ok().body(tripService.getTripDetailsPageByUser(principalDetails.getUser(), pageRequest));
    }

    @Operation(summary = "Trip 수정", description = "TripPatchDto를 통해 Trip 수정. TripPatchDto.userIdList, TripPatchDto.planIdList를 조정해 참여중인 User, 포함된 Plan을 추가, 삭제 할 수 있음. 수정하고싶은 attribute만 포함해서 보내야함!! ")
    @PatchMapping
    public ResponseEntity<TripDto.TripResponseDto> updateTrip(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody TripDto.TripPatchDto tripPatchDto) {
        return ResponseEntity.ok().body(tripService.updateTrip(principalDetails.getUser(),tripPatchDto));
    }

    @Operation(summary = "Trip 삭제", description = "trip id를 통해 trip 삭제")
    @DeleteMapping
    public ResponseEntity<String> deleteTrip(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Schema(description = "trip id",example = "1")
            @RequestParam @NotNull @Min(1) Long tripId) {
        return ResponseEntity.ok().body(tripService.deleteTrip(principalDetails.getUser(),tripId));
    }




}
