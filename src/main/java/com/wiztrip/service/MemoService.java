package com.wiztrip.service;

import com.wiztrip.constant.Category;
import com.wiztrip.domain.MemoEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.MemoDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.mapstruct.MemoMapper;
import com.wiztrip.repository.MemoRepository;
import com.wiztrip.repository.TripUserRepository;
import com.wiztrip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemoService {

    private final MemoMapper memoMapper;

    private final MemoRepository memoRepository;

    private final TripUserRepository tripUserRepository;

    private final TripRepository tripRepository;

    @Transactional
    public MemoDto.MemoResponseDto createMemo(UserEntity user, Long tripId, MemoDto.MemoPostDto memoPostDto) {
        checkValidByUser(user.getId(), tripId);
        checkTripIsFinished(tripId);
        return memoMapper.toResponseDto(memoRepository.save(memoMapper.toEntity(user, memoPostDto, tripId)));
    }

    public ListDto<MemoDto.MemoResponseDto> getMemoByCategory(UserEntity user, Long tripId, Category category) {
        checkValidByUser(user.getId(), tripId);
        List<MemoEntity> memoList = memoRepository.findAllByTripIdAndCategory(tripId, category);

        return new ListDto<>(memoList.stream().map(o -> {
            checkValidByTrip(o, tripId);
            return memoMapper.toResponseDto(o);
        }).toList());
    }

    @Transactional
    public MemoDto.MemoResponseDto updateMemo(UserEntity user, Long tripId, MemoDto.MemoPatchDto memoPatchDto) {
        checkValidByUser(user.getId(), tripId);
        MemoEntity memo = memoRepository.findById(memoPatchDto.getMemoId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));
        checkValidByTrip(memo, tripId);
        checkTripIsFinished(tripId);
        memoMapper.updateFromPatchDto(memoPatchDto, memo);
        return memoMapper.toResponseDto(memo);
    }

    @Transactional
    public String deleteMemo(UserEntity user, Long tripId, Long memoId) {
        checkValidByUser(user.getId(), tripId);
        MemoEntity memo = memoRepository.findById(memoId)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));
        checkValidByTrip(memo, tripId);
        checkTripIsFinished(tripId);
        memoRepository.deleteById(memoId);
        return "memoId: " + memoId + " 삭제 완료";
    }

    // 해당 trip에 속한 memo인지 확인
    private void checkValidByTrip(MemoEntity memo, Long tripId) {
        if (!memo.getTrip().getId().equals(tripId)) throw new CustomException(ErrorCode.NOT_IN_TRIP_MEMO);
    }

    // 해당 trip에 속한 user인지 확인
    private void checkValidByUser(Long userId, Long tripId) {
        if(!tripUserRepository.existsByUserIdAndTripId(userId, tripId)) throw new CustomException(ErrorCode.FORBIDDEN_TRIP_USER);
    }

    // trip의 종료 여부 확인
    private void checkTripIsFinished(Long tripId) {
        if (tripRepository.findById(tripId).orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND)).isFinished()) {
            throw new CustomException(ErrorCode.ALREADY_TRIP_FINISHED);
        }
    }
}
