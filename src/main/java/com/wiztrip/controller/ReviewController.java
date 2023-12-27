package com.wiztrip.controller;

import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 후기글 생성
    @PostMapping("trips/{tripId}/reviews")
    @Operation(summary = "Review 생성",
            description = "tripId와 ReviewPostDto를 사용해 해당 Trip(전체 여행 계획)에 속한 Review를 생성합니다.")
    public ResponseEntity<ReviewDto.ReviewResponseDto> createReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestBody ReviewDto.ReviewPostDto reviewPostDto) {
        return ResponseEntity.ok().body(reviewService.createReview(principalDetails.getUser(), tripId, reviewPostDto));
    }

    // 후기글 조회 (세부 사항)
    @GetMapping("trips/{tripId}/reviews")
    @Operation(summary = "Review 조회(세부 사항)",
            description = "tripId와 reviewId를 사용해 선택된 Review를 조회합니다.")
    public ResponseEntity<ReviewDto.ReviewResponseDto> getReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestParam("reviewId") Long reviewId) {
        return ResponseEntity.ok().body(reviewService.getReview(principalDetails.getUser(), tripId, reviewId));
    }

    // 후기글 조회 (전체, 마이페이지)
    @GetMapping("my-reviews")
    @Operation(summary = "Review 조회(마이 페이지)",
            description = """
                마이 페이지에서 해당 User가 작성한 review를 Trip(전체 여행 계획)의 startDate를 기준으로 전달합니다.
                페이징을 사용해 한 페이지에 몇 개의 Review를 받고 싶은지 설정할 수 있고, 원하는 페이지에 속한 Review를 확인할 수 있습니다.
                """
    )
    public ResponseEntity<Page<ReviewDto.MyReviewResponseDto>> getMyReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        // 정렬 기준 (전체 여행 계획(Trip)을 기준)
        Sort sort = Sort.by(Sort.Direction.DESC, "trip.startDate");

        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sort);
        return ResponseEntity.ok().body(reviewService.getMyReview(principalDetails.getUser(), pageRequest));
    }

    // 후기글 수정
    @PatchMapping("trips/{tripId}/reviews")
    @Operation(summary = "Review 수정",
            description = "tripId와 ReviewPatchDto를 사용해 해당 Review를 삭제합니다.")
    public ResponseEntity<ReviewDto.ReviewResponseDto> updateReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestBody ReviewDto.ReviewPatchDto reviewPatchDto) {
        return ResponseEntity.ok().body(reviewService.updateReview(principalDetails.getUser(), tripId, reviewPatchDto));
    }

    // 후기글 삭제
    @DeleteMapping("trips/{tripId}/reviews")
    @Operation(summary = "Review 삭제",
            description = "tripId와 reviewId를 사용해 해당 Review를 삭제합니다.")
    public ResponseEntity<String> deleteReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestParam("reviewId") Long reviewId) {
        return ResponseEntity.ok().body(reviewService.deleteReview(principalDetails.getUser(), tripId, reviewId));
    }
}
