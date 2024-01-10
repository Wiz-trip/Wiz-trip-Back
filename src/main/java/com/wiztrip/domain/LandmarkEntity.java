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

    @NotNull
    private String name;

    @Lob
    private String content;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "landmark", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LandmarkImageEntity> imageList = new ArrayList<>();

}
