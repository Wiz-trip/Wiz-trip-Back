package com.wiztrip.controller;

import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.dto.LikeboxDto;
import com.wiztrip.dto.ListDto;
import com.wiztrip.service.LikeboxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like")
@RestController
@RequiredArgsConstructor
@RequestMapping("like")
public class LikeController {

    private final LikeboxService likeboxService;

    // 여행지 좋아요 기능 //한개만 가능
    @Operation(summary = "Like 추가", description = "LikePostDto를 통한 Like 추가. JWT Token 입력 필수!!!")
    @PostMapping
    public ResponseEntity<String> addLike(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody LikeboxDto.LikePostDto likePostDto) {
        return ResponseEntity.ok().body(likeboxService.addLike(principalDetails.getUser(), likePostDto));
    }

    @Operation(summary = "Like 여러개 추가", description = "LikeAllPostDto를 통한 Like 한번에 여러개 추가. JWT Token 입력 필수!!!")
    @PostMapping("/all")
    // 여행지 좋아요 기능 //여러개 가능
    public ResponseEntity<String> addAllLike(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody LikeboxDto.LikeAllPostDto likeAllPostDto) {
        return ResponseEntity.ok().body(likeboxService.addAllLike(principalDetails.getUser(), likeAllPostDto));
    }

    // 좋아요 표시한 랜드마크의 id list 리턴
    @Operation(summary = "Like landmark id list 조회", description = "Like한 Landmark의 id list 조회. JWT Token 입력 필수!!!")
    @GetMapping
    public ResponseEntity<ListDto<Long>> getLikeList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(likeboxService.getLikeList(principalDetails.getUser()));
    }

    @Operation(summary = "like landmark detail list 조회",description = "Like한 Landmark의 details 조회. JWT Token 입력 필수!!!")
    @GetMapping("/with-details")
    public ResponseEntity<LikeboxDto.LikeDetailResponseDto> getLikeListWithLandmarkDetails(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(likeboxService.getLikeListWithLandmarkDetails(principalDetails.getUser()));
    }

    @Operation(summary = "Like 취소", description = "landmark id를 통한 Like 취소. JWT Token 입력 필수!!!")
    @DeleteMapping
    // 여행지 좋아요 취소 기능
    public ResponseEntity<String> deleteLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Schema(description = "landmark id", example = "1")
            @RequestParam Long landmarkId) {
        return ResponseEntity.ok().body(likeboxService.deleteLike(principalDetails.getUser(), landmarkId));
    }


}

