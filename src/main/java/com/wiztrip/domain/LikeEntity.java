package com.wiztrip.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LikeEntity {

    @Id
    @Column(name = "like_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "like", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LandmarkLikeEntity> landmarkLikeEntityList = new ArrayList<>();

    public LikeEntity(UserEntity user) {
        this.user = user;
    }


}
