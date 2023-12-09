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
//@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;

    private final LandmarkRepository landmarkRepository;

    private final LandmarkLikeRepository landmarkLikeRepository;


    // 여행지 좋아요 기능 //하나씩 가능

    @Transactional
    public String addLike(UserEntity user, LikeDto.LikePostDto likePostDto) {
        LikeEntity like = likeRepository.findByUserId(user.getId()).orElse(createLike(user));
        LandmarkEntity landmark = landmarkRepository.findById(likePostDto.getLandmarkId()).orElseThrow();

        if (isExistsByLandmarkIdAndLikeId(landmark.getId(), like.getId()))
            throw new RuntimeException("이미 좋아요 됨");

        LandmarkLikeEntity landmarkLike = new LandmarkLikeEntity(landmark, like);
        like.getLandmarkLikeEntityList().add(landmarkLike);

        return "userId: " + user.getId() + "\n"
                + "landmarkId: " + likePostDto.getLandmarkId() + " like 추가 완료";
    }
    // 여행지 좋아요 기능 //여러개 가능

    @Transactional
    public String addAllLike(UserEntity user, LikeDto.LikeAllPostDto likeAllPostDto) {
        LikeEntity like = likeRepository.findByUserId(user.getId()).orElse(createLike(user));

        List<Long> addSuccesslandmarkIdList = new ArrayList<>();
        List<Long> addFailedlandmarkIdList = new ArrayList<>();
        landmarkRepository.findAllById(likeAllPostDto.getLandmarkIdList()).forEach(o -> {
            if (isExistsByLandmarkIdAndLikeId(o.getId(), like.getId())) addFailedlandmarkIdList.add(o.getId());
            else {
                like.getLandmarkLikeEntityList().add(new LandmarkLikeEntity(o,like));
                addSuccesslandmarkIdList.add(o.getId());
            }
        });

        if(addSuccesslandmarkIdList.isEmpty()) throw new RuntimeException();
        return "userId: " + user.getId() + "\n"
                + "landmarkId: " + addSuccesslandmarkIdList + " like 추가 완료\n"
                + "landmarkId: " + addFailedlandmarkIdList + "like에 이미 있음";
    }
    //좋아요한 랜드마크의 id List 리턴

    public ListDto getLikeList(UserEntity user) {
        LikeEntity like = likeRepository.findByUserId(user.getId()).orElse(createLike(user));
        List<Long> list = like.getLandmarkLikeEntityList().stream()
                .map(o -> o.getLandmark().getId()).sorted().toList(); //todo: 좋아요한 시간의 내림차순으로 정렬해야함
        return new ListDto(list);
    }

    //todo: getLikeListWithLandmarkDetails() 만들어야할듯 --> LandmarkMapper 생성 후
    // 여행지 좋아요 취소 기능

    @Transactional
    public String deleteLike(UserEntity user, Long landmarkId) {
        List<LandmarkLikeEntity> landmarkLikeEntityList = likeRepository.findByUserId(user.getId()).orElseThrow().getLandmarkLikeEntityList();
        if (!landmarkLikeEntityList.removeIf(o -> o.getLandmark().getId().equals(landmarkId)))
            return "userId: " + user.getId() + "\nlandmarkId: " + landmarkId + "\nlike 가 존재하지 않습니다.";
        return "userId: " + user.getId() + "\nlandmarkId: " + landmarkId + "\nlike 삭제 완료";
    }

    private boolean isExistsByLandmarkIdAndLikeId(Long landmarkId, Long likeId) {
        return landmarkLikeRepository.existsByLandmarkIdAndLikeId(likeId,landmarkId);
    }

    private LikeEntity createLike(UserEntity user) {
        //만약 user에게 LikeEntity가 배정이 안되어 있다면
        if (!likeRepository.existsByUserId(user.getId())) {
            LikeEntity like = new LikeEntity(user);
            likeRepository.save(like);
            return like;
        }
        return likeRepository.findByUserId(user.getId()).orElseThrow();
    }
}
