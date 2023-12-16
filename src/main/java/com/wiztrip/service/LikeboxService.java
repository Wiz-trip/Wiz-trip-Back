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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeboxService {

    private final LikeboxRepository likeboxRepository;

    private final LandmarkRepository landmarkRepository;

    private final LandmarkLikeboxRepository landmarkLikeboxRepository;

    private final LandmarkMapper landmarkMapper;


    // 여행지 좋아요 기능 //여러개 가능
    @Transactional
    public Map<Long, String> addLike(UserEntity user, LikeboxDto.LikePostDto likePostDto) {

        LikeboxEntity likebox = findOrCreateLikebox(user);

        Map<Long, String> resultMap = new LinkedHashMap<>();
        landmarkRepository.findAllById(likePostDto.getLandmarkIdList()).forEach(o->{
            if(add(likebox, o)) resultMap.put(o.getId(), "done");
            else resultMap.put(o.getId(), "already exists");
        });

        return resultMap;
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
            return "userId: " + user.getId() + "\nlandmarkId: " + landmarkId + "\nlike 가 존재하지 않습니다."; //todo: throw로 바꾸기
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

    private boolean add(LikeboxEntity likebox, LandmarkEntity landmark) {
        if (isExistsByLandmarkIdAndLikeboxId(landmark.getId(), likebox.getId())) return false;
        likebox.getLandmarkLikeboxEntityList().add(new LandmarkLikeboxEntity(landmark, likebox));
        return true;
    }
}
