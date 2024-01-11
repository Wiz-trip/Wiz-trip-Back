package com.wiztrip.dto;

import com.wiztrip.constant.Address;
import lombok.*;

import java.util.List;



public class LandmarkDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LandmarkImageDto {

        private Long imageId;

        private String imageName;

        private String imagePath;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LandmarkAllResponseDto {

        private Long landmarkId;

        private String name;

        private Address address;

        private List<LandmarkImageDto> imageList;

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

        private List<LandmarkImageDto> imageList;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LandmarkApiResponseDto {


        private String address;

        private String imagePath;

        private Long contentId;

        private Long contentTypeId;

        private String title;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LandmarkApiDetailResponseDto {


        private String address;

        private String imagePath;

        private Long contentId;

        private Long contentTypeId;

        private String title;

        private String cat1;

        private String cat2;

        private String cat3;

    }


}