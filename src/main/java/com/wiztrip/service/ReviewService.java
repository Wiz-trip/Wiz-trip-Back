package com.wiztrip.service;

import com.wiztrip.domain.ReviewEntity;
import com.wiztrip.domain.ReviewImageEntity;
import com.wiztrip.domain.TripEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.mapstruct.ReviewMapper;
import com.wiztrip.repository.ReviewImageRepository;
import com.wiztrip.repository.ReviewRepository;
import com.wiztrip.repository.TripRepository;
import com.wiztrip.repository.TripUserRepository;
import com.wiztrip.tool.file.FtpTool;
import com.wiztrip.tool.file.WebpConvertTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewMapper reviewMapper;

    private final ReviewRepository reviewRepository;

    private final ReviewImageRepository reviewImageRepository;

    private final TripRepository tripRepository;

    private final TripUserRepository tripUserRepository;

    private final WebpConvertTool webpConvertTool;

    private final FtpTool ftpTool;

    @Transactional
    public ReviewDto.ReviewResponseDto createReview(UserEntity user, Long tripId, ReviewDto.ReviewPostDto reviewPostDto) {
        checkValidByUser(user.getId(), tripId);
        checkValidByTripAndUser(tripId, user);
        checkTripIsFinshied(tripId);
        ReviewEntity review = reviewMapper.toEntity(user, tripId, reviewPostDto);
        reviewRepository.save(review);
        checkValidByTrip(review, tripId);
        return reviewMapper.toResponseDto(review);
    }

    @Transactional
    public ReviewDto.ReviewResponseDto createReviewImage(UserEntity user, Long tripId, Long reviewId, List<MultipartFile> multipartFileList) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        checkWriteByUser(review, user);
        checkValidByUser(user.getId(), tripId);
        checkValidByTrip(review, tripId);
        checkTripIsFinshied(tripId);
        uploadImage(review, multipartFileList);
        return reviewMapper.toResponseDto(review);
    }

    public ReviewDto.ReviewResponseDto getReview(UserEntity user, Long tripId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        checkWriteByUser(review, user);
        checkValidByTrip(review, tripId);
        return reviewMapper.toResponseDto(review);
    }

    public ListDto<ReviewDto.MyReviewResponseDto> getMyReview(UserEntity user) {
        List<ReviewEntity> reviewList = reviewRepository.findByUserId(user.getId());

        return new ListDto<>(reviewList.stream().map(o -> {
            checkWriteByUser(o, user);
            return reviewMapper.toMyResponseDto(o);
        }).toList());
    }

    public ReviewDto.MyReviewCountResponseDto getMyReviewCount(UserEntity user) {
        return reviewMapper.toMyCountResponseDto(reviewRepository.countByUserId(user.getId()));
    }

    public ReviewDto.ToReviewCountResponseDto getToReviewCount(UserEntity user) {
        Integer reviewCount = tripRepository.countByUserIdAndFinishedTrue(user.getId()) - reviewRepository.countByUserId(user.getId());
        return reviewMapper.toToCountResponseDto(reviewCount);
    }

    @Transactional
    public ReviewDto.ReviewResponseDto updateReview(UserEntity user, Long tripId, ReviewDto.ReviewPatchDto reviewPatchDto) {
        ReviewEntity review = reviewRepository.findById(reviewPatchDto.getReviewId())
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        checkWriteByUser(review, user);
        checkValidByUser(user.getId(), tripId);
        checkValidByTrip(review, tripId);
        checkTripIsFinshied(tripId);
        reviewMapper.updateFromPatchDto(reviewPatchDto, review);
        return reviewMapper.toResponseDto(review);
    }

    @Transactional
    public String deleteReview(UserEntity user, Long tripId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        List<ReviewImageEntity> reviewImage = reviewImageRepository.findByReviewId(reviewId);

        checkWriteByUser(review, user);
        checkValidByUser(user.getId(), tripId);
        checkValidByTrip(review, tripId);
        checkTripIsFinshied(tripId);

        reviewRepository.deleteById(reviewId);

        for (ReviewImageEntity image : reviewImage) {
            reviewImageRepository.deleteById(image.getId());
            ftpTool.deleteFile(image.getImageName());
        }

        return "reviewId: " + reviewId + " 삭제 완료";
    }

    // 이미지(multipart) 업로드
    @Transactional
    public void uploadImage(ReviewEntity review, List<MultipartFile> multipartFileList) {
        for (MultipartFile multipartFile : multipartFileList) {

            String imageName = getUniqueImageName(multipartFile.getOriginalFilename());
            String imagePath = "wiztrip/" + imageName;

            InputStream webpInputStream = webpConvertTool.multipartFileToWebpInputStream(multipartFile);
            ftpTool.uploadInputStream(imageName, webpInputStream);

            ReviewImageEntity reviewImageEntity = new ReviewImageEntity();
            reviewImageEntity.setImagePath(imagePath);
            reviewImageEntity.setImageName(imageName);
            reviewImageRepository.save(reviewImageEntity);

            review.addImage(reviewImageEntity);

        }
    }

    // 이미지 중복 이름 확인
    private String getUniqueImageName(String originalFilename) {
        String imageName;

        do {
            imageName = java.util.UUID.randomUUID().toString();
        } while (reviewImageRepository.existsByImageName(imageName));

        return imageName + ".webp";
    }

    // 해당 trip에 속한 review인지 확인
    private void checkValidByTrip(ReviewEntity review, Long tripId) {
        if (!review.getTrip().getId().equals(tripId)) throw new CustomException(ErrorCode.NOT_IN_TRIP_REVIEW);
    }

    // 해당 trip에 속한 user인지 확인
    private void checkValidByUser(Long userId, Long tripId) {
        if(!tripUserRepository.existsByUserIdAndTripId(userId, tripId)) throw new CustomException(ErrorCode.FORBIDDEN_TRIP_USER);
    }

    // 해당 review를 작성한 user인지 확인
    private void checkWriteByUser(ReviewEntity review, UserEntity user) {
        if (!user.getId().equals(review.getUser().getId())) throw new CustomException(ErrorCode.FORBIDDEN_REVIEW_USER);
    }

    // trip 완료 여부 확인
    private void checkTripIsFinshied(Long tripId){
        TripEntity trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND));

        if (!trip.isFinished()) {throw new CustomException(ErrorCode.NOT_FINISHED_TRIP);}
    }

    // 하나의 trip, user 당 작성한 review가 있는지 확인
    private void checkValidByTripAndUser(Long tripId, UserEntity user){
        if (reviewRepository.existsByTripIdAndUserId(tripId, user.getId())) {
            throw new CustomException(ErrorCode.ALREADY_WRITTEN_REVIEW);
        }
    }
}