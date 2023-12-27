package com.wiztrip.dto;

import com.wiztrip.constant.Image;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class ReviewDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewPostDto {

        private List<Image> imageList;

        private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewPatchDto {

        private Long reviewId;

        private List<Image> imageList;

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

        private List<Image> imageList;

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

        private List<Image> imageList;
    }
}
