package com.wiztrip.service;

import com.wiztrip.domain.ReviewEntity;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.mapstruct.ReviewMapper;
import com.wiztrip.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private ReviewMapper reviewMapper;

    private ReviewRepository reviewRepository;


    public ReviewDto.ReviewResponseDto createReview(Long tripId, ReviewDto.ReviewPostDto reviewRequestDto) {
    }

    public ReviewDto.ReviewResponseDto getReview(Long tripId, Long reviewId) {
    }

    public ReviewDto.ReviewResponseDto updateReview(Long tripId, Long reviewId, ReviewDto.ReviewPatchDto reviewRequestDto) {
    }

    public String deleteReview(Long tripId, Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) return "reviewId: " + reviewId + "  존재하지 않는 review입니다.";
        checkValid(reviewRepository.findById(reviewId).orElseThrow(),tripId);
        reviewRepository.deleteById(reviewId);
        return "reviewId: " + reviewId + " 삭제 완료";
    }

    // 해당 trip에 속한 review인지 확인
    private static void checkValid(ReviewEntity review, Long tripId) {
        if (!review.getTrip().getId().equals(tripId)) throw new RuntimeException("Permission denied");
    }
}
