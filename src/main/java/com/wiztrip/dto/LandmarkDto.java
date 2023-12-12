package com.wiztrip.dto;

import com.wiztrip.constant.Address;
import com.wiztrip.constant.Image;
import lombok.*;

import java.util.List;



public class LandmarkDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LandmarkAllResponseDto {

        private Long landmarkId;

        private String name;

        private Address address;

        private List<String> imageList;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LandmarkDetailResponseDto {
        private Long landmarkId;

        private String name;

        private String content;     // 여행지 눌렀을 때 상세 설명

        private Address address;

        private List<String> imageList;

    }
}