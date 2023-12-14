package com.wiztrip.controller;

import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.dto.LikeDto;
import com.wiztrip.dto.ListDto;
import com.wiztrip.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("like")
public class LikeController {

     private final LikeService likeService;

     // 여행지 좋아요 기능 //한개만 가능
     @PostMapping
     public ResponseEntity<String> addLike(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody LikeDto.LikePostDto likePostDto) {
          return ResponseEntity.ok().body(likeService.addLike(principalDetails.getUser(), likePostDto));
     }

     @PostMapping("/all")
     // 여행지 좋아요 기능 //여러개 가능
     public ResponseEntity<String> addAllLike(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody LikeDto.LikeAllPostDto likeAllPostDto) {
          return ResponseEntity.ok().body(likeService.addAllLike(principalDetails.getUser(), likeAllPostDto));
     }

     // 좋아요 표시한 랜드마크의 id list 리턴
     @GetMapping
     public ResponseEntity<ListDto<Long>> getLikeList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
          return ResponseEntity.ok().body(likeService.getLikeList(principalDetails.getUser()));
     }

     @DeleteMapping
     // 여행지 좋아요 취소 기능
     public ResponseEntity<String> deleteLike(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestParam Long landmarkId) {
          return ResponseEntity.ok().body(likeService.deleteLike(principalDetails.getUser(), landmarkId));
     }


}

