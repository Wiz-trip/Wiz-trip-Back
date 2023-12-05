package com.wiztrip.dto;


import com.wiztrip.domain.LandmarkLikeEntity;
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
    public static class PostLikeDto {

        private Long id;

        private UserEntity user;

        private List<LandmarkLikeEntity> landmarkLikeEntityList = new ArrayList<>();
    }

}
