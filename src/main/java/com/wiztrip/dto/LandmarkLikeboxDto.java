package com.wiztrip.dto;

import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.domain.LikeboxEntity;
import lombok.*;

public class LandmarkLikeboxDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LandmarkLikePostDto {

        private Long id;

        private LandmarkEntity landmark;

        private LikeboxEntity like;

    }

}
