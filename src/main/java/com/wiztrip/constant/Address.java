package com.wiztrip.constant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Embeddable
@Getter
//값타입은 불변객체인게 유리하므로 Setter는 만들지 말아야함
public class Address {
    @Schema(description = "도로명 주소", example = "서울특별시 중구 세종대로 110")
    private String roadNameAddress; //도로명 주소

    @Schema(description = "지번 주소", example = "태평로1가 31 서울특별시청")
    private String localAddress; //지번 주소

    protected Address() {} //무분별한 생성을 막기 위해 protected로 선언

    //필요시 추가 생성자 필요
    public Address(String roadNameAddress, String localAddress) {
        this.roadNameAddress = roadNameAddress;
        this.localAddress = localAddress;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getRoadNameAddress(), address.getRoadNameAddress()) && Objects.equals(getLocalAddress(), address.getLocalAddress());
    }

}
