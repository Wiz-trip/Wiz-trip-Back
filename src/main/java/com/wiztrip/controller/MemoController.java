package com.wiztrip.controller;

import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.MemoDto;
import com.wiztrip.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("trips/{tripId}/memos")
public class MemoController {

    private MemoService memoService;

    // 메모 생성
    @PostMapping
    public ResponseEntity<MemoDto.MemoResponseDto> createMemo(@PathVariable("trip_id") Long tripId, @RequestBody MemoDto.MemoPostDto memoPostDto) {
        return ResponseEntity.ok().body(memoService.createMemo(tripId, memoPostDto));
    }

    // 메모 조회 (전체 전달)
    @GetMapping
    public ResponseEntity<ListDto> getMemoAll(@PathVariable("trip_id") Long tripId) {
        return ResponseEntity.ok().body(memoService.getMemoAll(tripId));
    }

    // 메모 수정
    @PatchMapping
    public ResponseEntity<MemoDto.MemoResponseDto> updateMemo(@PathVariable("trip_id") Long tripId, @RequestBody MemoDto.MemoPatchDto memoPatchDto) {
        return ResponseEntity.ok().body(memoService.updateMemo(tripId, memoPatchDto));
    }

    // 메모 삭제
    @DeleteMapping("/{memoId}")
    public ResponseEntity<String> deleteMemo(@PathVariable("trip_id") Long tripId, @PathVariable("memo_id") Long memoId) {
        return ResponseEntity.ok().body(memoService.deleteMemo(tripId, memoId));
    }
}
