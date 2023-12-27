package com.wiztrip.controller;

import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 후기글 생성
    @PostMapping("trips/{tripId}/reviews")
    public ResponseEntity<ReviewDto.ReviewResponseDto> createReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestBody ReviewDto.ReviewPostDto reviewPostDto) {
        return ResponseEntity.ok().body(reviewService.createReview(principalDetails.getUser(), tripId, reviewPostDto));
    }

    // 후기글 조회 (세부 사항)
    @GetMapping("trips/{tripId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDto.ReviewResponseDto> getReview(@PathVariable("tripId") Long tripId, @PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok().body(reviewService.getReview(tripId, reviewId));
    }

    // 후기글 조회 (전체, 마이페이지)
    @GetMapping("my-reviews")
    public ResponseEntity<Page<ReviewDto.MyReviewResponseDto>> getMyReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        // 정렬 기준 (전체 여행 계획(Trip)을 기준)
        Sort sort = Sort.by(Sort.Direction.ASC, "trip.startDate");

        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sort);
        return ResponseEntity.ok().body(reviewService.getMyReview(principalDetails.getUser(), pageRequest));
    }

    // 후기글 수정
    @PatchMapping("trips/{tripId}/reviews")
    public ResponseEntity<ReviewDto.ReviewResponseDto> updateReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestBody ReviewDto.ReviewPatchDto reviewPatchDto) {
        return ResponseEntity.ok().body(reviewService.updateReview(principalDetails.getUser(), tripId, reviewPatchDto));
    }

    // 후기글 삭제
    @DeleteMapping("trips/{tripId}/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok().body(reviewService.deleteReview(principalDetails.getUser(), tripId, reviewId));
    }
}
