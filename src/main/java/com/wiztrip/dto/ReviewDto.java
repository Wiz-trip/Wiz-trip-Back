package com.wiztrip.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReviewDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewImageDto {

        private Long imageId;

        private String imageName;

        private String imagePath;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewPostDto {

        private String content;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewPatchDto {

        private Long reviewId;

        // 수정할 image의 imageId 받아오기
        private List<Long> imageIdList = new ArrayList<>();

        private String content;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewResponseDto {

        private Long reviewId;

        private Long tripId;

        private Long userId;

        private List<ReviewImageDto> imageList = new ArrayList<>();

        private String content;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MyReviewResponseDto {

        private Long reviewId;

        private Long tripId;

        private Long userId;

        private String destination;

        private LocalDate startDate;

        private LocalDate finishDate;

        private List<ReviewImageDto> imageList = new ArrayList<>();

    }
}
