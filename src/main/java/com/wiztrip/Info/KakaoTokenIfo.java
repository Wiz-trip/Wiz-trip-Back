package com.wiztrip.Info;


import lombok.Getter;
import lombok.Setter;


// OAuth 서버로 부터 받은 응답을  java 객체로 매핑하기 위해
@Getter
@Setter
public class KakaoTokenIfo {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;

}
