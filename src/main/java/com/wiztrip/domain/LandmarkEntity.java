package com.wiztrip.domain;

import com.wiztrip.constant.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class LandmarkEntity {

    @Id
    @Column(name = "landmark_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long contentId;

    private Long contentTypeId;

    @NotNull
    private String name;

    @Lob
    private String content;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "landmark", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LandmarkImageEntity> imageList = new ArrayList<>();

    private String areaCode;

    private String infocenter;  // Tel number

    private String restDate;    // 쉬는 날

    private String accomcount;  // 수용 인원

    private String useTime;    // 영업 시간

    private String parking;     // 주차 가능 여부

    private String checkPet;    // 애완동물 출입 가능 여부

    private String checkCreditCard; // 신용카드 가능 여부

}
