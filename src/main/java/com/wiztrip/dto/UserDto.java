package com.wiztrip.dto;

import com.wiztrip.tool.file.Base64Dto;
import lombok.*;


public class UserDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserResponseDto {

        private Long id;

        private String username;

        private String email;

        private Base64Dto image; // 프로필 사진

        private String nickname; // 회원 닉네임

        private LikeboxDto like;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserPatchDto {

        private Long id;

        private String username;

        private String email;

        private String nickname; // 회원 닉네임

        private LikeboxDto like;

    }
}
