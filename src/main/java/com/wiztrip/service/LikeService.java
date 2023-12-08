package com.wiztrip.service;


import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.domain.LandmarkLikeEntity;
import com.wiztrip.domain.LikeEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.LikeDto;
import com.wiztrip.dto.ListDto;
import com.wiztrip.repository.LandmarkLikeRepository;
import com.wiztrip.repository.LandmarkRepository;
import com.wiztrip.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;

    private final LandmarkRepository landmarkRepository;

    private final LandmarkLikeRepository landmarkLikeRepository;

    @Transactional
    public LikeEntity createLike(UserEntity user) {
        //만약 user에게 LikeEntity가 배정이 안되어 있다면
        if (!likeRepository.existsByUserId(user.getId())) {
            LikeEntity like = new LikeEntity(user);
            likeRepository.save(like);
            return like;
        }
        return null;
    }

    // 여행지 좋아요 기능 //하나씩 가능
    @Transactional
    public String addLike(UserEntity user, LikeDto.LikePostDto likePostDto) {
        LikeEntity like = likeRepository.findByUserId(user.getId()).orElse(createLike(user));
        LandmarkEntity landmark = landmarkRepository.findById(likePostDto.getLandmarkId()).orElseThrow();
        landmarkLikeRepository.save(new LandmarkLikeEntity(landmark,like));
        return "userId: "+user.getId()+"\nlandmarkId: "+likePostDto.getLandmarkId()+" \nlike 추가 완료";
    }

    // 여행지 좋아요 기능 //여러개 가능
    @Transactional
    public String addAllLike(UserEntity user, LikeDto.LikeAllPostDto likeAllPostDto) {
        LikeEntity like = likeRepository.findByUserId(user.getId()).orElse(createLike(user));
        likeAllPostDto.getLandmarkIdList().forEach(o->{
            LandmarkEntity landmark = landmarkRepository.findById(o).orElseThrow();
            landmarkLikeRepository.save(new LandmarkLikeEntity(landmark,like));
        });
        return "userId: "+user.getId()+"\nlandmarkId: "+likeAllPostDto.getLandmarkIdList().toString()+" \nlike 추가 완료";
    }

    //좋아요한 랜드마크의 id List 리턴
    public ListDto getLikeList(UserEntity user) {
        LikeEntity like = likeRepository.findByUserId(user.getId()).orElse(createLike(user));
        List<Long> list = new ArrayList<>();
        like.getLandmarkLikeEntityList().forEach(o->list.add(o.getLandmark().getId()));
        return new ListDto(list);
    }

    // 여행지 좋아요 취소 기능
    @Transactional
    public String deleteLike(UserEntity user, Long landmarkId) {
        LikeEntity like = likeRepository.findByUserId(user.getId()).orElse(createLike(user));
        landmarkLikeRepository.deleteById(like.getLandmarkLikeEntityList().stream()
                .filter(o -> o.getLandmark().getId().equals(landmarkId))
                .findFirst().orElseThrow().getId());
        return "userId: "+user.getId()+"\nlandmarkId: "+landmarkId+"\nlike 삭제 완료";
    }
}
