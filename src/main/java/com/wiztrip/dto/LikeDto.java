package com.wiztrip.dto;


import com.wiztrip.domain.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class LikeDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LikePostDto {
        @Schema(description = "like할 landmark id", example = "1")
        private Long landmarkId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LikeAllPostDto {
        @Schema(description = "like할 landmark id의 list", example = "[\n" +
                "    1\n" +
                "  ]")
        private List<Long> landmarkIdList = new ArrayList<>();
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LikeResponseDto {
        @Schema(description = "like id", example = "1")
        private Long likeId;

        @Schema(description = "user id", example = "1")
        private UserEntity userId;

        @Schema(description = "like할 landmark id의 list", example = "[\n" +
                "    1\n" +
                "  ]")
        private List<Long> landmarkIdList = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LikeDetailResponseDto {
        @Schema(description = "like id", example = "1")
        private Long likeId;

        @Schema(description = "user id", example = "1")
        private UserEntity userId;

        @Schema(description = "landmark detail list")
        private List<LandmarkDto.LandmarkDetailResponseDto> landmarkDetailResponseDtoList = new ArrayList<>();
    }
}

