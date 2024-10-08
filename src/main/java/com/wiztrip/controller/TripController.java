package com.wiztrip.controller;

import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.TripDto;
import com.wiztrip.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
public class TripController {

    private final TripService tripService;

    @Operation(summary = "Trip 생성", description = "TripPostDto를 사용해 Trip 생성. userIdList에 본인은 넣지 않아도 됨.")
    @PostMapping("/trips")
    public ResponseEntity<TripDto.TripResponseDto> createTrip(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid TripDto.TripPostDto tripPostDto) {
        return ResponseEntity.ok().body(tripService.createTrip(principalDetails.getUser(), tripPostDto));
    }

    // trip에 user 추가
    @Operation(summary = "Trip에 User 추가", description = "tripId와 userId를 통해 해당 Trip에 User를 추가할 수 있습니다. 종료된 Trip의 경우, User를 추가할 수 없습니다.")
    @PostMapping("/trips/{tripId}/users/{userId}")
    public ResponseEntity<TripDto.TripUserResponseDto> createTripUser(
            @PathVariable("tripId") Long tripId,
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(tripService.createTripUser(tripId, userId));
    }

    @Operation(summary = "Trip 조회", description = "trip id를 통해 Trip 정보 조회")
    @GetMapping("/trips")
    public ResponseEntity<TripDto.TripResponseDto> getTrip(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Schema(description = "trip id", example = "1")
            @RequestParam @NotNull @Min(1) Long tripId) {
        return ResponseEntity.ok().body(tripService.getTrip(principalDetails.getUser(),tripId));
    }

    @Operation(summary = "User가 속한 Trip id 조회 - Page",description = "User가 속한 Trip의 id를 담은 Page 조회. JWT Token 필수!!!")
    @GetMapping("/trips/page")
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
    @GetMapping("/trips/with-details/page")
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

    @Operation(summary = "User가 속한 Trip detail 조회",description = "User가 속한 Trip의 Detail을 담은 Page 조회. JWT Token 필수!!!")
    @GetMapping("/trips/with-details")
    public ResponseEntity<ListDto<TripDto.TripResponseDto>> getTripDetailsByUser(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(tripService.getTripDetailsByUser(principalDetails.getUser()));
    }

    // 전체 여행 계획 조회 (마이페이지, 계획 중인 여행 개수)
    @Operation(summary = "Trip 조회(계획 중인 여행 개수)",
            description = "마이 페이지에서 해당 User가 계획 중인 여행 개수(아직 종료하지 않은 Trip 개수)를 전달합니다.")
    @GetMapping("my-trips/count")
    public ResponseEntity<TripDto.MyTripCountResponseDto> getMyTripCount(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(tripService.getMyTripCount(principalDetails.getUser()));
    }

    @Operation(summary = "Trip 수정", description = "TripPatchDto를 통해 Trip 수정. TripPatchDto.userIdList, TripPatchDto.planIdList를 조정해 참여중인 User, 포함된 Plan을 추가, 삭제 할 수 있음. 수정하고싶은 attribute만 포함해서 보내야함!!")
    @PatchMapping("/trips")
    public ResponseEntity<TripDto.TripResponseDto> updateTrip(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody @Valid TripDto.TripPatchDto tripPatchDto) {
        return ResponseEntity.ok().body(tripService.updateTrip(principalDetails.getUser(),tripPatchDto));
    }

    @Operation(summary = "Trip 삭제", description = "trip id를 통해 trip 삭제합니다. Owner(Trip을 생성한 사람)만 삭제할 수 있습니다.")
    @DeleteMapping("/trips")
    public ResponseEntity<String> deleteTrip(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Schema(description = "trip id",example = "1")
            @RequestParam @NotNull @Min(1) Long tripId) {
        return ResponseEntity.ok().body(tripService.deleteTrip(principalDetails.getUser(),tripId));
    }

    // 공유 url 생성
    @Operation(summary = "Trip 공유 url 생성",
            description = "tripId를 통해 10글자의 랜덤한 url을 생성합니다. 유효 기간이 지났을 경우, 새로운 url을 생성하고, 만약 유효 기간이 지나지 않았다면, 이전에 생성한 url을 반환합니다. " +
                    "종료된 Trip의 경우, url을 생성할 수 없습니다.")
    @PostMapping("/trips/share")
    public ResponseEntity<TripDto.TripUrlResponseDto> createTripUrl (
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam Long tripId) {
        return ResponseEntity.ok().body(tripService.createTripUrl(principalDetails.getUser(), tripId));
    }

    // 공유 url 조회
    @Operation(summary = "Trip 공유 url 조회",
            description = "user를 고려하지 않고, url을 통해 해당하는 tripId를 조회합니다.")
    @GetMapping("/trips/share")
    public ResponseEntity<TripDto.TripIdResponseDto> getTripUrl (
            @RequestParam String url) {
        return ResponseEntity.ok().body(tripService.getTripUrl(url));
    }

    // 전체 여행 계획 종료
    @Operation(summary = "Trip 종료",
            description = "tripId를 통해 해당 Trip의 상태를 false(기본값)에서 true로 변경합니다. Owner(Trip을 생성한 사람)만 종료할 수 있고, 종료할 경우, 더 이상 Trip을 수정할 수 없으며, Plan과 Memo의 생성, 수정, 삭제가 불가능합니다.")
    @PatchMapping("/trips/{tripId}")
    public ResponseEntity<String> updateTripFinish (
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId) {
        return ResponseEntity.ok().body(tripService.updateTripFinish(principalDetails.getUser(), tripId));
    }
}
