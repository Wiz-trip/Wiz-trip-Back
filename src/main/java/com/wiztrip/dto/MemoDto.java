package com.wiztrip.dto;

import com.wiztrip.constant.Category;
import lombok.*;

public class MemoDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemoPostDto {

        private String title;

        private String content;

        private String url;

        private Category category;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemoPatchDto {

        private Long memoId;

        private String title;

        private String content;

        private String url;

        private Category category;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemoResponseDto {

        private Long memoId;

        private Long tripId;

        private String title;

        private String content;

        private String url;

        private Category category;
    }
}
