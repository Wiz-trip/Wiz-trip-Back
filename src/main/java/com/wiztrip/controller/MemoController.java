package com.wiztrip.controller;

import com.wiztrip.config.spring_security.auth.PrincipalDetails;
import com.wiztrip.constant.Category;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.MemoDto;
import com.wiztrip.service.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Memo")
@RestController
@RequiredArgsConstructor
@RequestMapping("trips/{tripId}/memos")
public class MemoController {

    private final MemoService memoService;

    // 메모 생성
    @PostMapping
    @Operation(summary = "Memo 생성",
            description = "tripId와 MemoPostDto를 사용해 해당 Trip(전체 여행 계획)에 속한 Memo를 생성합니다.")
    public ResponseEntity<MemoDto.MemoResponseDto> createMemo(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestBody MemoDto.MemoPostDto memoPostDto) {
        return ResponseEntity.ok().body(memoService.createMemo(principalDetails.getUser(), tripId, memoPostDto));
    }

    // 메모 조회 (카테고리별)
    @GetMapping
    @Operation(summary = "Memo 조회",
            description = """
                tripId를 사용해 해당 Trip(전체 여행 계획)에 작성된 카테고리별 Memo를 전달합니다.
                페이징을 사용해 한 페이지에 몇 개의 Memo를 받고 싶은지 설정할 수 있고, 원하는 페이지에 속한 Memo를 확인할 수 있습니다.
                """
    )
    public ResponseEntity<ListDto<MemoDto.MemoResponseDto>> getMemoByCategory(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestParam Category category) {
        return ResponseEntity.ok().body(memoService.getMemoByCategory(principalDetails.getUser(), tripId, category));
    }

    // 메모 수정
    @PatchMapping
    @Operation(summary = "Memo 수정",
            description = "tripId와 MemoPatchDto를 사용해 해당 Memo를 삭제합니다.")
    public ResponseEntity<MemoDto.MemoResponseDto> updateMemo(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestBody MemoDto.MemoPatchDto memoPatchDto) {
        return ResponseEntity.ok().body(memoService.updateMemo(principalDetails.getUser(), tripId, memoPatchDto));
    }

    // 메모 삭제
    @DeleteMapping
    @Operation(summary = "Memo 삭제",
            description = "tripId와 memoId를 사용해 해당 Memo를 삭제합니다.")
    public ResponseEntity<String> deleteMemo(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("tripId") Long tripId,
            @RequestParam("memoId") Long memoId) {
        return ResponseEntity.ok().body(memoService.deleteMemo(principalDetails.getUser(), tripId, memoId));
    }
}