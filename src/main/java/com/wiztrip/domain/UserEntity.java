package com.wiztrip.domain;

import com.wiztrip.constant.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String username; //로그인 아이디

    @NotNull
    private String password;

    @NotNull
    private String email;

    @Embedded
    private Image image; //프로필 사진

    private String nickname; //회원 닉네임

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private LikeEntity like; //좋아요 누른 랜드마크
}
