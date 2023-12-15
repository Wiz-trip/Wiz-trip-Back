package com.wiztrip.controller;

import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.MemoDto;
import com.wiztrip.service.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Memo")
@RestController
@RequiredArgsConstructor
@RequestMapping("trips/{trip_id}/memos")
public class MemoController {

    private final MemoService memoService;

    // 메모 생성
    @PostMapping
    @Operation(summary = "Memo 생성",
            description = "trip_id와 MemoPostDto를 사용해 해당 Trip(전체 여행 계획)에 속한 Memo를 생성합니다.")
    public ResponseEntity<MemoDto.MemoResponseDto> createMemo(@PathVariable("trip_id") Long tripId, @RequestBody MemoDto.MemoPostDto memoPostDto) {
        return ResponseEntity.ok().body(memoService.createMemo(tripId, memoPostDto));
    }

    // 메모 조회 (전체 전달)
    @GetMapping
    @Operation(summary = "Memo 조회",
            description = "trip_id를 사용해 카테고리 상관 없이 해당 Trip(전체 여행 계획)에 작성된 모든 Memo를 전달합니다. " +
                    "만약 아직 작성된 Memo가 없을 경우, NOT_FOUND(404) 예외를 보내도록 처리했습니다.")
    public ResponseEntity<ListDto<MemoDto.MemoResponseDto>> getMemoAll(@PathVariable("trip_id") Long tripId) {
        return ResponseEntity.ok().body(memoService.getMemoAll(tripId));
    }

    // 메모 수정
    @PatchMapping
    @Operation(summary = "Memo 수정",
            description = "trip_id와 MemoPatchtDto를 사용해 해당 Memo를 삭제합니다.")
    public ResponseEntity<MemoDto.MemoResponseDto> updateMemo(@PathVariable("trip_id") Long tripId, @RequestBody MemoDto.MemoPatchDto memoPatchDto) {
        return ResponseEntity.ok().body(memoService.updateMemo(tripId, memoPatchDto));
    }

    // 메모 삭제
    @DeleteMapping("/{memo_id}")
    @Operation(summary = "Memo 삭제",
            description = "trip_id와 memo_id를 사용해 해당 Memo를 삭제합니다.")
    public ResponseEntity<String> deleteMemo(@PathVariable("trip_id") Long tripId, @PathVariable("memo_id") Long memoId) {
        return ResponseEntity.ok().body(memoService.deleteMemo(tripId, memoId));
    }
}