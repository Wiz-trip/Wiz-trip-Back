package com.wiztrip.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
    private String password;

}
