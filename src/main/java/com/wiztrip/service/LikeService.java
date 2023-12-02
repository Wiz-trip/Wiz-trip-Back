package com.wiztrip.service;


import com.wiztrip.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private LikeRepository likeRepository;

    // 여행지 좋아요 기능



    // 여행지 좋아요 취소 기능


}
