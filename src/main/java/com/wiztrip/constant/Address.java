package com.wiztrip.constant;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Embeddable
@Getter
//값타입은 불변객체인게 유리하므로 Setter는 만들지 말아야함
public class Address {
    private String dou;     //도
    private String si;      //시
    private String gun;     //군
    private String gu;      //구
    private String dong;    //동
    private String eup;     //읍
    private String myeon;   //면
    private String ro;      //로
    private String gil;     //길

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
