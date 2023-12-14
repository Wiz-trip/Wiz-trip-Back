package com.wiztrip.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LandmarkLikeboxEntity extends TimeStamp{

    @Id
    @Column(name = "landmark_likebox_entity")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landmark_id")
    private LandmarkEntity landmark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "likebox_id")
    private LikeboxEntity likebox;

    public LandmarkLikeboxEntity(LandmarkEntity landmark, LikeboxEntity likebox) {
        this.landmark = landmark;
        this.likebox = likebox;
    }
}
