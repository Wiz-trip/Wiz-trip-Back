package com.wiztrip.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {
// 회원가입 시 필요 : 이메일, 닉네임, 비밀번호(+재확인)

    private String username;

    private String email;

    private String password;

    @NotBlank(message = "재확인 비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,}$", message = "비밀번호 조건 불만족")
    private String confirmPassword;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$",message = "닉네임은 한/영 2자이상 8자이하로 입력해주세요")
    private String nickname;

}
