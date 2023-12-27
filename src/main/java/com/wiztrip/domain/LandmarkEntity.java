package com.wiztrip.domain;

import com.wiztrip.constant.Address;
import com.wiztrip.constant.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "landmark_images",
            joinColumns = @JoinColumn(name = "landmark_id")
    )
    private List<Image> imageList = new ArrayList<>();

    @ManyToMany(mappedBy = "bookmarkedLandmarks")
    private Set<UserEntity> bookmarkedByUsers = new HashSet<>();
}
