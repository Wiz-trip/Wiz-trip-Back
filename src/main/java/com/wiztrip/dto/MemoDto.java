package com.wiztrip.dto;

import com.wiztrip.constant.Category;
import lombok.*;

public class MemoDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class MemoPostDto {

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
    public class MemoPatchDto {

        private String title;

        private String content;

        private String url;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class MemoResponseDto {

        private Long memoId;

        private String title;

        private String content;

        private String url;

        private Category category;
    }
}
