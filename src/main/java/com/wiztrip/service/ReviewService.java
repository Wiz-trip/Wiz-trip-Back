package com.wiztrip.service;

import com.wiztrip.domain.ReviewEntity;
import com.wiztrip.domain.ReviewImageEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.mapstruct.ReviewMapper;
import com.wiztrip.repository.ReviewImageRepository;
import com.wiztrip.repository.ReviewRepository;
import com.wiztrip.tool.file.FtpTool;
import com.wiztrip.tool.file.WebpConvertTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    private final WebpConvertTool webpConvertTool;

    private final FtpTool ftpTool;

    @Transactional
    public ReviewDto.ReviewResponseDto createReview(UserEntity user, Long tripId, ReviewDto.ReviewPostDto reviewPostDto, List<MultipartFile> multipartFileList) {
        uploadImage(multipartFileList);
        return reviewMapper.toResponseDto(reviewRepository.save(reviewMapper.toEntity(user, tripId, reviewPostDto)));
    }

    public ReviewDto.ReviewResponseDto getReview(UserEntity user, Long tripId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        checkValidByTrip(review, tripId);
        checkValidByUser(review, user);
        return reviewMapper.toResponseDto(review);
    }

    public Page<ReviewDto.MyReviewResponseDto> getMyReview(UserEntity user, PageRequest pageRequest) {
        Page<ReviewEntity> reviewPage = reviewRepository.findByUser(user, pageRequest);

        return reviewPage.map(o -> {
            checkValidByUser(o, user);
            return reviewMapper.toMyResponseDto(o);
        });
    }

    @Transactional
    public ReviewDto.ReviewResponseDto updateReview(UserEntity user, Long tripId, ReviewDto.ReviewPatchDto reviewPatchDto) {
        ReviewEntity review = reviewRepository.findById(reviewPatchDto.getReviewId())
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        checkValidByTrip(review, tripId);
        checkValidByUser(review, user);

        reviewMapper.updateFromPatchDto(reviewPatchDto, review);
        return reviewMapper.toResponseDto(review);
    }

    @Transactional
    public String deleteReview(UserEntity user, Long tripId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        checkValidByTrip(review,tripId);
        checkValidByUser(review, user);
        reviewRepository.deleteById(reviewId);
        return "reviewId: " + reviewId + " 삭제 완료";
    }

    // 이미지(multipartFile) 추가
    private void uploadImage(List<MultipartFile> multipartFileList){
        for (MultipartFile multipartFile : multipartFileList) {

            String imageName = getUniqueImageName(multipartFile.getOriginalFilename());
            String imagePath = "wiztrip/" + imageName;

            InputStream webpInputStream = webpConvertTool.multipartFileToWebpInputStream(multipartFile);
            ftpTool.uploadInputStream(imageName, webpInputStream);

            ReviewImageEntity reviewImageEntity = new ReviewImageEntity();
            reviewImageEntity.setImagePath(imagePath);
            reviewImageEntity.setImageName(imageName);
            reviewImageRepository.save(reviewImageEntity);
        }
    }

    // 이미지 중복 이름 확인
    private String getUniqueImageName(String originalFilename) {
        String imageName = originalFilename;

        while (reviewImageRepository.existsByImageName(imageName)) {
            imageName = java.util.UUID.randomUUID().toString();
        }

        return imageName + ".webp";
    }

    // 해당 trip에 속한 review인지 확인
    private static void checkValidByTrip(ReviewEntity review, Long tripId) {
        if (!review.getTrip().getId().equals(tripId)) throw new CustomException(ErrorCode.NOT_IN_TRIP_REVIEW);
    }

    // 해당 review를 작성한 user인지 확인
    private static void checkValidByUser(ReviewEntity review, UserEntity user) {
        if (!user.getId().equals(review.getUser().getId())) throw new CustomException(ErrorCode.FORBIDDEN_REVIEW_USER);
    }
}