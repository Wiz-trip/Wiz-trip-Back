package com.wiztrip.domain;

import com.wiztrip.constant.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class UserEntity extends TimeStamp {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)      // 값이 중복되지 않도록 설정
    private String username; //로그인 아이디

    @NotNull
    private String password;       // 회원가입 시 필요

    @NotNull
    @Column(unique = true)      // 값이 중복되지 않도록 설정
    private String email;       // 회원가입 시 필요

    @Embedded
    private Image image; //프로필 사진

    private String nickname; //회원 닉네임

    @ManyToMany
    @JoinTable(
            name = "user_bookmarks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "landmark_id")
    )
    private Set<LandmarkEntity> bookmarkedLandmarks = new HashSet<>();
}
