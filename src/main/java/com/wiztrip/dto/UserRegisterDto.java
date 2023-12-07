package com.wiztrip.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {
// 회원가입 시 필요 : 이메일, 닉네임, 비밀번호(+재확인)

    private String email;

    private String password;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,}$", message = "비밀번호 조건 불만족")
    private String confirmPassword;

    private String nickname;

}
