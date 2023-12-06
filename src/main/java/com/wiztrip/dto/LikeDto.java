package com.wiztrip.dto;


import com.wiztrip.domain.UserEntity;
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
        private Long landmarkId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LikeAllPostDto {
        private List<Long> landmarkIdList = new ArrayList<>();
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LikeResponseDto {
        private Long id;

        private UserEntity userId;

        private List<Long> landmarkIdList = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LikeDetailResponseDto {
        private Long id;

        private UserEntity userId;

        private List<LandmarkDto.LandmarkDetailResponseDto> landmarkDetailResponseDtoList = new ArrayList<>();
    }
}

