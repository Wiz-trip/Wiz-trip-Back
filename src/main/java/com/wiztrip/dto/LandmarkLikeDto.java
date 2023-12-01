package com.wiztrip.dto;

import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.domain.LikeEntity;
import jakarta.persistence.*;
import lombok.*;

public class LandmarkLikeDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LandmarkLikePostDto {

        private Long id;

        private LandmarkEntity landmark;

        private LikeEntity like;

    }

}
