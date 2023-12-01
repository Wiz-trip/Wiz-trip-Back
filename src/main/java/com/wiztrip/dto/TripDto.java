package com.wiztrip.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TripPostDto {
        private LocalDate startDate;

        private LocalDate finishDate;

        private List<Long> userIdList = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TripResponseDto {
        private Long tripId;

        private LocalDate startDate;

        private LocalDate finishDate;

        private List<Long> userIdList = new ArrayList<>();

        private List<Long> planIdList = new ArrayList<>(); //만약 여러개의 plan을 한번에 삭제하려고 할 때 필요
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TripPatchDto {
        private Long tripId;

        private LocalDate startDate;

        private LocalDate finishDate;

        private List<Long> userIdList = new ArrayList<>(); //참여중인 유저가 변동될 경우 필요

        private List<Long> planIdList = new ArrayList<>(); //만약 여러개의 plan을 한번에 삭제하려고 할 때 필요
    }

}
