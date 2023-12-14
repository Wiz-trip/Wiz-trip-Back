package com.wiztrip.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wiztrip.constant.Address;
import com.wiztrip.constant.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class PlanDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PlanPostDto {

        @Schema(example = "제육집")
        private String name; //계획 이름. ~~식당, ~~호텔 등

        private Address address;

        @Schema(description = "시작 일자,시간", type = "string",
                pattern = "2023(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])T(0[0-9]|1[0-2]):([0-5][0-9])")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime startTime;

        @Schema(description = "종료 일자,시간", type = "string",
                pattern = "2023(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])T(0[0-9]|1[0-2]):([0-5][0-9])")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime finishTime;

        @Schema(example = "제육 맛집")
        private String content; //plan 설명

        @Schema(example = "RESTAURANT")
        private Category category;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PlanResponseDto {
        @Schema(example = "1")
        private Long planId;

        @Schema(example = "제육집")
        private String name; //계획 이름. ~~식당, ~~호텔 등

        private Address address;

        @Schema(description = "시작 일자,시간", type = "string",
                pattern = "2023(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])T(0[0-9]|1[0-2]):([0-5][0-9])")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime startTime;

        @Schema(description = "종료 일자,시간", type = "string",
                pattern = "2023(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])T(0[0-9]|1[0-2]):([0-5][0-9])")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime finishTime;

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

        private LocalDateTime startTime;

        private LocalDateTime finishTime;

        private String content; //plan 설명

        private Category category;
    }


}
