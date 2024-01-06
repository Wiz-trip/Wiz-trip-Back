package com.wiztrip.domain;

import com.wiztrip.constant.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class MemoEntity {

    @Id
    @Column(name = "memo_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 200)
    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String url;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private TripEntity trip;
}
