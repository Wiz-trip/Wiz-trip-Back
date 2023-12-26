package com.wiztrip.service;

import com.wiztrip.domain.ReviewEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
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
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        checkValid(review, tripId);
        return reviewMapper.toResponseDto(review);
    }

    @Transactional
    public ReviewDto.ReviewResponseDto updateReview(UserEntity user, Long tripId, ReviewDto.ReviewPatchDto reviewPatchDto) {
        ReviewEntity review = reviewRepository.findById(reviewPatchDto.getReviewId())
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        checkValid(review, tripId);
        checkUser(user, review);
        reviewMapper.updateFromPatchDto(reviewPatchDto, review);
        return reviewMapper.toResponseDto(review);
    }

    @Transactional
    public String deleteReview(UserEntity user, Long tripId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        checkValid(review,tripId);
        checkUser(user, review);
        reviewRepository.deleteById(reviewId);
        return "reviewId: " + reviewId + " 삭제 완료";
    }

    // 해당 trip에 속한 review인지 확인
    private static void checkValid(ReviewEntity review, Long tripId) {
        if (!review.getTrip().getId().equals(tripId)) throw new CustomException(ErrorCode.NOT_IN_TRIP_REVIEW);
    }

    // 해당 review를 작성한 user인지 확인
    private static void checkUser(UserEntity user, ReviewEntity review) {
        if (!user.getId().equals(review.getUser().getId())) throw new CustomException(ErrorCode.FORBIDDEN_REVIEW_USER);
    }
}