package com.wiztrip.service;

import com.wiztrip.constant.Category;
import com.wiztrip.domain.MemoEntity;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.MemoDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.mapstruct.MemoMapper;
import com.wiztrip.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemoService {

    private final MemoMapper memoMapper;

    private final MemoRepository memoRepository;

    @Transactional
    public MemoDto.MemoResponseDto createMemo(Long tripId, MemoDto.MemoPostDto memoPostDto) {
        return memoMapper.toResponseDto(memoRepository.save(memoMapper.toEntity(memoPostDto, tripId)));
    }

    // 원하는 데이터 담아서 변환해준 후, ListDto를 통해 리스트화 필요
    public Map<String, Object> getMemoByCategory(Long tripId, Category category, PageRequest pageRequest) {
        Page<MemoEntity> memoPage = memoRepository.findAllByTripIdAndCategory(tripId, category, pageRequest);

        List<MemoDto.MemoResponseDto> memoList = memoPage.getContent().stream()
                .map(o -> {
                    checkValid(o, tripId);
                    return memoMapper.toResponseDto(o);
                })
                .toList();

        Map<String, Object> memoResponse = new HashMap<>();
        memoResponse.put("totalElement", memoPage.getTotalElements());
        memoResponse.put("totalPage", memoPage.getTotalPages());
        memoResponse.put("list", memoList);

        return memoResponse;
    }

    @Transactional
    public MemoDto.MemoResponseDto updateMemo(Long tripId, MemoDto.MemoPatchDto memoPatchDto) {
        MemoEntity memo = memoRepository.findById(memoPatchDto.getMemoId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));
        checkValid(memo, tripId);
        memoMapper.updateFromPatchDto(memoPatchDto, memo);
        return memoMapper.toResponseDto(memo);
    }

    @Transactional
    public String deleteMemo(Long tripId, Long memoId) {
        if (!memoRepository.existsById(memoId)) {
            throw new CustomException(ErrorCode.MEMO_NOT_FOUND);
        }
        checkValid(memoRepository.findById(memoId).orElseThrow(), tripId);
        memoRepository.deleteById(memoId);
        return "memoId: " + memoId + " 삭제 완료";
    }

    // 해당 trip에 속한 memo인지 확인
    private static void checkValid(MemoEntity memo, Long tripId) {
        if (!memo.getTrip().getId().equals(tripId)) throw new CustomException(ErrorCode.NOT_IN_TRIP_MEMO);
    }
}
