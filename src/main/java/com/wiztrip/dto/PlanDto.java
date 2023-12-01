package com.wiztrip.dto;

import com.wiztrip.constant.Address;
import com.wiztrip.constant.Category;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

public class PlanDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PlanPostDto {
        private String name; //계획 이름. ~~식당, ~~호텔 등

        private Address address;

        private LocalTime startTime;

        private LocalTime finishTime;

        private String content; //plan 설명

        private Category category;

//        private Long user; //등록한 유저 //@AuthenticationPrincipal로 처리 가능
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PlanResponseDto {
        private Long planId;

        private String name; //계획 이름. ~~식당, ~~호텔 등

        private Address address;

        private LocalTime startTime;

        private LocalTime finishTime;

        private String content; //plan 설명

        private Category category;

        private Long userId; //등록한 유저의 id

        private Long tripId; //plan이 속한 trip의 id
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PlanPatchDto {
        @NotNull
        private Long planId;

        private String name; //계획 이름. ~~식당, ~~호텔 등

        private Address address;

        private LocalTime startTime;

        private LocalTime finishTime;

        private String content; //plan 설명

        private Category category;
    }


}
