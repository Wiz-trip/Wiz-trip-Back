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
        private Long id;

        private String name;

        private Address address;

        private List<Image> imageList;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LandmarkDetailResponseDto {
        private Long id;

        private String name;

        private Address address;

        private List<Image> imageList;

    }


}
