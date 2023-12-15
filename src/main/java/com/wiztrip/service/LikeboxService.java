package com.wiztrip.service;


import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.domain.LandmarkLikeboxEntity;
import com.wiztrip.domain.LikeboxEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.LandmarkDto;
import com.wiztrip.dto.LikeboxDto;
import com.wiztrip.dto.ListDto;
import com.wiztrip.mapstruct.LandmarkMapper;
import com.wiztrip.repository.LandmarkLikeboxRepository;
import com.wiztrip.repository.LandmarkRepository;
import com.wiztrip.repository.LikeboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeboxService {

    private final LikeboxRepository likeboxRepository;

    private final LandmarkRepository landmarkRepository;

    private final LandmarkLikeboxRepository landmarkLikeboxRepository;

    private final LandmarkMapper landmarkMapper;


    // 여행지 좋아요 기능 //하나씩 가능
    @Transactional
    public String addLike(UserEntity user, LikeboxDto.LikePostDto likePostDto) {
        LikeboxEntity likebox = findOrCreateLikebox(user);
        LandmarkEntity landmark = landmarkRepository.findById(likePostDto.getLandmarkId()).orElseThrow();

        if (isExistsByLandmarkIdAndLikeboxId(landmark.getId(), likebox.getId()))
            throw new RuntimeException("이미 좋아요 됨");

        likebox.getLandmarkLikeboxEntityList().add(new LandmarkLikeboxEntity(landmark, likebox));

        return "userId: " + user.getId() + "\n"
                + "landmarkId: " + likePostDto.getLandmarkId() + " like 추가 완료";
    }

    // 여행지 좋아요 기능 //여러개 가능
    @Transactional
    public String addAllLike(UserEntity user, LikeboxDto.LikeAllPostDto likeAllPostDto) {

        LikeboxEntity likebox = findOrCreateLikebox(user);

        List<Long> addSuccesslandmarkIdList = new ArrayList<>();
        List<Long> addFailedlandmarkIdList = new ArrayList<>();
        landmarkRepository.findAllById(likeAllPostDto.getLandmarkIdList()).forEach(o -> {
            if (isExistsByLandmarkIdAndLikeboxId(o.getId(), likebox.getId())) addFailedlandmarkIdList.add(o.getId());
            else {
                likebox.getLandmarkLikeboxEntityList().add(new LandmarkLikeboxEntity(o, likebox));
                addSuccesslandmarkIdList.add(o.getId());
            }
        });

        if (addSuccesslandmarkIdList.isEmpty()) throw new RuntimeException();
        return "userId: " + user.getId() + "\n"
                + "landmarkId: " + addSuccesslandmarkIdList + " like 추가 완료\n"
                + "landmarkId: " + addFailedlandmarkIdList + "likebox에 이미 있음";
    }

    //좋아요한 랜드마크의 id List 리턴
    public ListDto<Long> getLikeList(UserEntity user) {
        if (!likeboxRepository.existsByUserId(user.getId())) return new ListDto<>(new ArrayList<>());

        Long likeboxId = likeboxRepository.findByUserId(user.getId()).orElseThrow().getId();
        return new ListDto<>(landmarkRepository.findAllIdByLikeboxId(likeboxId));
    }

    public Page<Long> getLikeListPage(UserEntity user, Pageable pageable) {
        if (!likeboxRepository.existsByUserId(user.getId())) return Page.empty(pageable);

        Long likeboxId = likeboxRepository.findByUserId(user.getId()).orElseThrow().getId();
        return landmarkRepository.findAllIdByLikeboxId(likeboxId, pageable);
    }

    public ListDto<LandmarkDto.LandmarkDetailResponseDto> getLikeListWithLandmarkDetails(UserEntity user) {
        if (!likeboxRepository.existsByUserId(user.getId())) return new ListDto<>(new ArrayList<>());

        return new ListDto<>(landmarkRepository.findAllByLikeboxId(likeboxRepository.findByUserId(user.getId()).orElseThrow().getId())
                .stream().map(landmarkMapper::entityToDetailResponseDto).toList());
    }

    public Page<LandmarkDto.LandmarkDetailResponseDto> getLikeListWithLandmarkDetailsPage(UserEntity user, Pageable pageable) {
        if (!likeboxRepository.existsByUserId(user.getId())) return Page.empty(pageable);

        Long likeboxId = likeboxRepository.findByUserId(user.getId()).orElseThrow().getId();
        return landmarkRepository.findAllByLikeboxId(likeboxId,pageable).map(landmarkMapper::entityToDetailResponseDto);
    }

    // 여행지 좋아요 취소 기능
    @Transactional
    public String deleteLike(UserEntity user, Long landmarkId) {
        List<LandmarkLikeboxEntity> landmarkLikeboxEntityList = likeboxRepository.findByUserId(user.getId()).orElseThrow().getLandmarkLikeboxEntityList();
        if (!landmarkLikeboxEntityList.removeIf(o -> o.getLandmark().getId().equals(landmarkId)))
            return "userId: " + user.getId() + "\nlandmarkId: " + landmarkId + "\nlike 가 존재하지 않습니다.";
        return "userId: " + user.getId() + "\nlandmarkId: " + landmarkId + "\nlike 삭제 완료";
    }

    private boolean isExistsByLandmarkIdAndLikeboxId(Long landmarkId, Long likeId) {
        return landmarkLikeboxRepository.existsByLandmarkIdAndLikeboxId(likeId, landmarkId);
    }

    private LikeboxEntity findOrCreateLikebox(UserEntity user) {
        return likeboxRepository.findByUserId(user.getId()).orElseGet(() -> {
            LikeboxEntity likebox = new LikeboxEntity(user);
            likeboxRepository.save(likebox);
            return likebox;
        });
    }
}
