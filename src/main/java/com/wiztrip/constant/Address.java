package com.wiztrip.constant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Embeddable
@Getter
//값타입은 불변객체인게 유리하므로 Setter는 만들지 말아야함
public class Address {
    @Schema(description = "00도",example = "경기도")
    private String dou;     //도
    @Schema(description = "00시",example = "서울시")
    private String si;      //시
    @Schema(description = "00군",example = "나주군")
    private String gun;     //군
    @Schema(description = "00구",example = "서대문구")
    private String gu;      //구
    @Schema(description = "00동",example = "죽전동")
    private String dong;    //동
    @Schema(description = "00읍",example = "조치원읍")
    private String eup;     //읍
    @Schema(description = "00면",example = "전의면")
    private String myeon;   //면
    @Schema(description = "00로",example = "세종대로")
    private String ro;      //로
    @Schema(description = "00길",example = "11길")
    private String gil;     //길

    @Schema(description = "위도",example = "37.5642135")
    private Double latitude;    //위도
    @Schema(description = "경도",example = "127.0016985")
    private Double longitude;   //경도

    protected Address() {
    } //무분별한 생성을 막기 위해 protected로 선언

    //todo: 추가 생성자 필요


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getDou(), address.getDou()) && Objects.equals(getSi(), address.getSi())
                && Objects.equals(getGun(), address.getGun()) && Objects.equals(getGu(), address.getGu())
                && Objects.equals(getDong(), address.getDong()) && Objects.equals(getEup(), address.getEup())
                && Objects.equals(getMyeon(), address.getMyeon()) && Objects.equals(getRo(), address.getRo())
                && Objects.equals(getGil(), address.getGil());
    }
}
