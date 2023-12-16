package com.wiztrip.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class LikeboxDto {

//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    public static class LikePostDto {
//        @Schema(description = "like할 landmark id", example = "1")
//        private Long landmarkId;
//    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LikePostDto {
        @Schema(description = "like할 landmark id의 list", example = "[\n" +
                "    1\n" +
                "  ]")
        private List<Long> landmarkIdList = new ArrayList<>();
    }
}

