package com.wiztrip.dto;

import com.wiztrip.constant.Image;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Image image; // 프로필 사진
    private String nickname; // 회원 닉네임
}
