package com.wiztrip.domain;

import com.wiztrip.constant.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter  @Setter
public class MemoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String url;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private TripEntity tripEntity;
}
