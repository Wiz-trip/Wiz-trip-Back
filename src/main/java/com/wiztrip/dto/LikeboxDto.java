package com.wiztrip.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class LikeboxDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LikePostDto {
        @Schema(description = "like할 landmark id의 list", example = "[\n" +
                "    1\n" +
                "  ]")
        @NotEmpty(message = "landmarkIdList는 비어 있을 수 없습니다.")
        private List<Long> landmarkIdList = new ArrayList<>();
    }
}

