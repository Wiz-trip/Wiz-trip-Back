package com.wiztrip.domain;

import com.wiztrip.constant.Address;
import com.wiztrip.constant.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PlanEntity { //Trip의 세부 사항. 장소, 시간, 예산 등

    @Id
    @Column(name = "plan_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    private Address address;

    private LocalDateTime startTime;

    private LocalDateTime finishTime;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content; //plan 설명

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user; //등록한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private TripEntity trip;


}
