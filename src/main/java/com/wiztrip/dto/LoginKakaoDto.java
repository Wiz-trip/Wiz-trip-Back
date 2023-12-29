package com.wiztrip.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginKakaoDto {

        private String email;
        private String nickname;

}
