package com.wiztrip.dto;

import com.wiztrip.constant.Category;
import lombok.*;

public class ReviewDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class ReviewPostDto {

        // 이미지 기능 구현 후 추가
        // private List<Image> imageList;

        private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class ReviewPatchDto {

        // 이미지 기능 구현 후 추가
        // private List<Image> imageList;

        private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class ReviewResponseDto {

        private Long reviewId;

        private Long tripId;

        private Long userId;

        // 이미지 기능 구현 후 추가
        // private List<Image> imageList;

        private String content;
    }
}
