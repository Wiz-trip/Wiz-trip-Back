package com.wiztrip.dto;

import com.wiztrip.tool.file.Base64Dto;
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

        // 수정할 image의 name 받아오기
        private List<String> fileNameList = new ArrayList<>();

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

        private List<Base64Dto> imageList = new ArrayList<>();

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

        private List<Base64Dto> imageList = new ArrayList<>();

    }
}
