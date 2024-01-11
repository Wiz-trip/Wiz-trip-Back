package com.wiztrip.Info;


import lombok.Getter;
import lombok.Setter;
import java.util.Map;


// be -> fe 데이터 보낼 때 사용
@Getter
@Setter
public class KakaoResponse {
    private String access_Token;
    private String refresh_Token;
    private Map<String, Object> userInfo;       // 사용자 정보
}
