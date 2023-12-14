package com.wiztrip.controller;

import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.dto.LikeDto;
import com.wiztrip.dto.ListDto;
import com.wiztrip.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(summary = "Like 추가", description = "LikePostDto를 통한 Like 추가. JWT Token 입력 필수!!!")
    @PostMapping
    public ResponseEntity<String> addLike(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody LikeDto.LikePostDto likePostDto) {
        return ResponseEntity.ok().body(likeService.addLike(principalDetails.getUser(), likePostDto));
    }

    @Operation(summary = "Like 여러개 추가", description = "LikeAllPostDto를 통한 Like 한번에 여러개 추가. JWT Token 입력 필수!!!")
    @PostMapping("/all")
    // 여행지 좋아요 기능 //여러개 가능
    public ResponseEntity<String> addAllLike(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody LikeDto.LikeAllPostDto likeAllPostDto) {
        return ResponseEntity.ok().body(likeService.addAllLike(principalDetails.getUser(), likeAllPostDto));
    }

    // 좋아요 표시한 랜드마크의 id list 리턴
    @Operation(summary = "Like list 조회", description = "Like한 Landmark의 id list 조회. JWT Token 입력 필수!!!")
    @GetMapping
    public ResponseEntity<ListDto<Long>> getLikeList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(likeService.getLikeList(principalDetails.getUser()));
    }

    @Operation(summary = "Like 취소", description = "landmark id를 통한 Like 취소. JWT Token 입력 필수!!!")
    @DeleteMapping
    // 여행지 좋아요 취소 기능
    public ResponseEntity<String> deleteLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Schema(description = "landmark id", example = "1")
            @RequestParam Long landmarkId) {
        return ResponseEntity.ok().body(likeService.deleteLike(principalDetails.getUser(), landmarkId));
    }


}

