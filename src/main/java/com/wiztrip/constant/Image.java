package com.wiztrip.constant;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Embeddable
@Getter
//값타입은 불변객체인게 유리하므로 Setter는 만들지 말아야함
public class Image {
    private String imageName; //name은 UUID등을 사용해서 중복되지 않도록 만들어야함
    private String imagePath;

    protected Image() {} //무분별한 생성을 막기 위해 protected로 선언

    public Image(String imageName, String imagePath) {
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(getImageName(), image.getImageName()) && Objects.equals(getImagePath(), image.getImagePath());
    }
}
