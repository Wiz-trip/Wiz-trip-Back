package com.wiztrip.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LandmarkLikeEntity {

    @Id
    @Column(name = "landmark_like_entity")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landmark_id")
    private LandmarkEntity landmark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_id")
    private LikeEntity like;

    public LandmarkLikeEntity(LandmarkEntity landmark, LikeEntity like) {
        this.landmark = landmark;
        this.like = like;
    }
}
