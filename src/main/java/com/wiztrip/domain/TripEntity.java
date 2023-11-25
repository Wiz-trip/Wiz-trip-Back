package com.wiztrip.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class TripEntity {

    @Id
    @Column(name = "trip_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate startDate;

    private LocalDate finishDate;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripUserEntity> tripUserEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanEntity> planEntityList = new ArrayList<>();
}
