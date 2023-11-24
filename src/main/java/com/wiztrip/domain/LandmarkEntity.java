package com.wiztrip.domain;

import com.wiztrip.constant.Address;
import com.wiztrip.constant.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class LandmarkEntity { //todo: PlanEntity와 연관관계를 가질건지?

    @Id
    @Column(name = "landmark_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @NotNull
    private String name;

    @Embedded
    private Address address;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "landmark_images",
            joinColumns = @JoinColumn(name = "landmark_id")
    )
    private List<Image> imageList = new ArrayList<>();
}
