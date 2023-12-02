package com.wiztrip.controller;

import com.wiztrip.dto.ReviewDto;
import com.wiztrip.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("trips/{tripId}/reviews")
public class ReviewController {

    private ReviewService reviewService;

    // 후기글 생성
    @PostMapping
    public ResponseEntity<ReviewDto.ReviewResponseDto> createReview(@PathVariable("trip_id") Long tripId, @RequestBody ReviewDto.ReviewPostDto reviewRequestDto) {
        return ResponseEntity.ok().body(reviewService.createReview(tripId, reviewRequestDto));
    }

    // 후기글 조회 (세부 사항)
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto.ReviewResponseDto> getReview(@PathVariable("trip_id") Long tripId, @PathVariable("review_id") Long reviewId) {
        return ResponseEntity.ok().body(reviewService.getReview(tripId, reviewId);
    }

    // 후기글 수정
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewDto.ReviewResponseDto> updateReview(@PathVariable("trip_id") Long tripId, @PathVariable("review_id") Long reviewId, @RequestBody ReviewDto.ReviewPatchDto reviewRequestDto) {
        return ResponseEntity.ok().body(reviewService.updateReview(tripId, reviewId, reviewRequestDto));
    }

    // 후기글 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("trip_id") Long tripId, @PathVariable("review_id") Long reviewId) {
        return ResponseEntity.ok().body(reviewService.deleteReview(tripId, reviewId));
    }
}
