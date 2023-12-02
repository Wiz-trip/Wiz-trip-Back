package com.wiztrip.service;

import com.wiztrip.constant.Category;
import com.wiztrip.domain.MemoEntity;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.MemoDto;
import com.wiztrip.mapstruct.MemoMapper;
import com.wiztrip.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemoService {

    private MemoMapper memoMapper;

    private MemoRepository memoRepository;

    public MemoDto.MemoResponseDto createMemo(Long tripId, MemoDto.MemoPostDto memoPostDto) {

    }

    // 원하는 데이터 담아서 변환해준 후, ListDto를 통해 리스트화 필요
    public ListDto getMemoByCategory(Long tripId, Category category) {

    }

    public MemoDto.MemoResponseDto updateMemo(Long tripId, Long memoId, MemoDto.MemoPatchDto memoPatchDto) {

    }

    public String deleteMemo(Long tripId, Long memoId) {
        if (!memoRepository.existsById(memoId)) return "memoId: " + memoId + "  존재하지 않는 memo입니다.";
        checkValid(memoRepository.findById(memoId).orElseThrow(),tripId);
        memoRepository.deleteById(memoId);
        return "memoId: " + memoId + " 삭제 완료";
    }

    // 해당 trip에 속한 memo인지 확인
    private static void checkValid(MemoEntity memo, Long tripId) {
        if (!memo.getTrip().getId().equals(tripId)) throw new RuntimeException("Permission denied");
    }
}
