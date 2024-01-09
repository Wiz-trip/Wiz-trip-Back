package com.wiztrip.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReviewImageEntity {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imageName;

    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

}
