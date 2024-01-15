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

        private Long contentId;

        private String address;

        private String imagePath;

        private Long contentTypeId;

        private String title;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LandmarkApiDetailResponseDto {

        private Long contentId;

        private String infocenter;  // Tel number

        private String restDate;    // 쉬는 날

        private String accomcount;  // 수용 인원

        private String useTime;    // 영업 시간

        private String parking;     // 주차 가능 여부

        private String checkPet;    // 애완동물 출입 가능 여부

        private String checkCreditCard; // 신용카드 가능 여부




    }


}