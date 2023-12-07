package com.wiztrip.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {


    @NotEmpty(message = "닉네임은 필수항목 입니다.")
    private String nickname;

    @NotEmpty(message = "이메일은 필수항목 입니다")
    @Email
    private String email;

    @NotEmpty(message = "비밀번호는 필수항목 입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{10,}$", message = "비밀번호는 영문, 숫자, 특수문자 중 2종류 이상을 조합하여 최소 10자리 이상이어야 합니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수항목 입니다.")
    private String confirmPassword;         // 비밀번호 확인
}
