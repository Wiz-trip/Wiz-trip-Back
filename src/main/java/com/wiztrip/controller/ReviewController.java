package com.wiztrip.controller;

import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("trips/{tripId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 후기글 생성
    @PostMapping
    public ResponseEntity<ReviewDto.ReviewResponseDto> createReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestBody ReviewDto.ReviewPostDto reviewPostDto) {
        return ResponseEntity.ok().body(reviewService.createReview(principalDetails.getUser(), tripId, reviewPostDto));
    }

    // 후기글 조회 (세부 사항)
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto.ReviewResponseDto> getReview(@PathVariable("tripId") Long tripId, @PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok().body(reviewService.getReview(tripId, reviewId));
    }

    // 후기글 수정
    @PatchMapping
    public ResponseEntity<ReviewDto.ReviewResponseDto> updateReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestBody ReviewDto.ReviewPatchDto reviewPatchDto) {
        return ResponseEntity.ok().body(reviewService.updateReview(principalDetails.getUser(), tripId, reviewPatchDto));
    }

    // 후기글 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok().body(reviewService.deleteReview(principalDetails.getUser(), tripId, reviewId));
    }
}
