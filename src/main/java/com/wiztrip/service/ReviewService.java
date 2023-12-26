package com.wiztrip.service;

import com.wiztrip.domain.ReviewEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.mapstruct.ReviewMapper;
import com.wiztrip.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewMapper reviewMapper;

    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewDto.ReviewResponseDto createReview(UserEntity user, Long tripId, ReviewDto.ReviewPostDto reviewPostDto) {
        return reviewMapper.toResponseDto(reviewRepository.save(reviewMapper.toEntity(user, tripId, reviewPostDto)));
    }

    public ReviewDto.ReviewResponseDto getReview(Long tripId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId).orElseThrow();
        checkValid(review, tripId);
        return reviewMapper.toResponseDto(review);
    }

    @Transactional
    public ReviewDto.ReviewResponseDto updateReview(Long tripId, ReviewDto.ReviewPatchDto reviewPatchDto) {
        ReviewEntity review = reviewRepository.findById(reviewPatchDto.getReviewId()).orElseThrow();
        checkValid(review, tripId);
        reviewMapper.updateFromPatchDto(reviewPatchDto, review);
        return reviewMapper.toResponseDto(reviewRepository.findById(reviewPatchDto.getReviewId()).orElseThrow());
    }

    @Transactional
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