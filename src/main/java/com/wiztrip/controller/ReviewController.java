package com.wiztrip.controller;

import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.ReviewDto;
import com.wiztrip.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Review")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 후기글 생성
    @PostMapping(value = "trips/{tripId}/reviews")
    @Operation(summary = "Review 생성(json)",
            description = "tripId와 ReviewPostDto를 사용해 해당 Trip(전체 여행 계획)에 속한 Review를 생성합니다. 이때, Review Image는 여러 장 삽입할 수 있습니다. " +
                    "또한, 종료된 Trip에 한해서 Review를 작성할 수 있으며, User는 자신이 속한 Trip에서 한 개의 Review만 작성할 수 있습니다.")
    public ResponseEntity<ReviewDto.ReviewResponseDto> createReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestBody ReviewDto.ReviewPostDto reviewPostDto) {
        return ResponseEntity.ok().body(reviewService.createReview(principalDetails.getUser(), tripId, reviewPostDto));
    }

    // 후기글 이미지 생성
    @PostMapping(value = "trips/{tripId}/reviews/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Review 생성(image)",
            description = "tripId와 ReviewPostDto를 사용해 해당 Trip(전체 여행 계획)에 속한 Review를 생성합니다. 이때, Review Image는 여러 장 삽입할 수 있습니다. " +
                    "또한, 종료된 Trip에 한해서 Review를 작성할 수 있으며, User는 자신이 속한 Trip에서 한 개의 Review만 작성할 수 있습니다.")
    public ResponseEntity<ReviewDto.ReviewResponseDto> createReviewImage(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @PathVariable("reviewId") Long reviewId,
            @RequestPart List<MultipartFile> multipartFileList) {
        return ResponseEntity.ok().body(reviewService.createReviewImage(principalDetails.getUser(), tripId, reviewId, multipartFileList));
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
    @Operation(summary = "Review 조회(나의 여행 기록)",
            description = "마이 페이지에서 해당 User가 작성한 Review 정보를 전달합니다.")
    public ResponseEntity<ListDto<ReviewDto.MyReviewResponseDto>> getMyReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(reviewService.getMyReview(principalDetails.getUser()));
    }

    // 후기글 조회 (마이페이지, 작성한 후기글 개수)
    @GetMapping("my-reviews/count")
    @Operation(summary = "Review 조회(작성한 후기글 개수)",
            description = "마이 페이지에서 해당 User가 작성한 Review 개수를 전달합니다.")
    public ResponseEntity<ReviewDto.MyReviewCountResponseDto> getMyReviewCount(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(reviewService.getMyReviewCount(principalDetails.getUser()));
    }

    // 후기글 조회 (마이페이지, 작성할 후기글 개수)
    @GetMapping("my-to-reviews/count")
    @Operation(summary = "Review 조회(작성할 후기글 개수)",
            description = "마이 페이지에서 해당 User가 작성할 수 있는 Review 개수를 전달합니다.")
    public ResponseEntity<ReviewDto.ToReviewCountResponseDto> getToReviewCount(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(reviewService.getToReviewCount(principalDetails.getUser()));
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
            description = "tripId와 reviewId를 사용해 해당 Review와 Review에 해당하는 Image를 삭제합니다.")
    public ResponseEntity<String> deleteReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestParam("reviewId") Long reviewId) {
        return ResponseEntity.ok().body(reviewService.deleteReview(principalDetails.getUser(), tripId, reviewId));
    }
}
