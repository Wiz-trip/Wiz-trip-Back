package com.wiztrip.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

        @Schema(description = "시작 일자", type = "string",
                pattern = "2023(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
        private LocalDate startDate;

        @Schema(description = "종료 일자", type = "string",
                pattern = "2023(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
        private LocalDate finishDate;

        @Schema(description = "목적지 이름", example = "제주도")
        @NotBlank(message = "목적지를 입력해주세요")
        private String destination; //목적지

        @Schema(description = "참여하는 User의 id.", example = "[\n" +
                "    1\n" +
                "  ]")
        private List<Long> userIdList = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TripResponseDto {
        @Schema(description = "trip id", example = "1")
        private Long tripId;

        @Schema(description = "시작 일자", type = "string",
                pattern = "2023(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
        private LocalDate startDate;

        @Schema(description = "종료 일자", type = "string",
                pattern = "2023(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
        private LocalDate finishDate;

        @Schema(description = "목적지 이름", example = "제주도")
        private String destination; //목적지

        @Schema(description = "참여하는 User의 id", example = "[\n" +
                "    1\n" +
                "  ]")
        private List<Long> userIdList = new ArrayList<>();

        @Schema(description = "Trip에 속한 Plan의 id list", example = "[\n" +
                "    1\n" +
                "  ]")
        private List<Long> planIdList = new ArrayList<>(); //만약 여러개의 plan을 한번에 삭제하려고 할 때 필요
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TripPatchDto {
        @Schema(description = "trip id", example = "1")
        @NotNull(message = "tripId를 입력해주세요.")
        private Long tripId;

        @Schema(description = "시작 일자", type = "string",
                pattern = "2023(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
        private LocalDate startDate;

        @Schema(description = "종료 일자", type = "string",
                pattern = "2023(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
        private LocalDate finishDate;

        @Schema(description = "목적지 이름", example = "제주도")
        private String destination; //목적지

        @Schema(description = "참여하는 User의 id", example = "[\n" +
                "    1\n" +
                "  ]")
        private List<Long> userIdList = new ArrayList<>(); //참여중인 유저가 변동될 경우 필요

        @Schema(description = "Trip에 속한 Plan의 id list", example = "[\n" +
                "    1\n" +
                "  ]")
        private List<Long> planIdList = new ArrayList<>(); //만약 여러개의 plan을 한번에 삭제하려고 할 때 필요
    }

}
