package com.wiztrip.service;

import com.wiztrip.domain.TripEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.TripDto;
import com.wiztrip.mapstruct.TripMapper;
import com.wiztrip.repository.TripRepository;
import com.wiztrip.repository.TripUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TripService {

    private final TripMapper tripMapper;

    private final TripRepository tripRepository;

    private final TripUserRepository tripUserRepository;

    @Transactional
    public TripDto.TripResponseDto createTrip(UserEntity user, TripDto.TripPostDto tripPostDto) {
        //본인이 userIdList에 포함되어 있지 않으면 추가
        if(!tripPostDto.getUserIdList().contains(user.getId())) tripPostDto.getUserIdList().add(user.getId());
        return tripMapper.toResponseDto(tripRepository.save(tripMapper.toEntity(tripPostDto)));
    }

    public TripDto.TripResponseDto getTrip(UserEntity user, Long tripId) {
        TripEntity trip = tripRepository.findById(tripId).orElseThrow();
        checkValid(user.getId(), trip.getId());
        return tripMapper.toResponseDto(trip);
    }

    public Page<Long> getTripIdPageByUser(UserEntity user, Pageable pageable) {
        return tripRepository.findAllTripIdByUserId(user.getId(), pageable);
    }

    public Page<TripDto.TripResponseDto> getTripDetailsPageByUser(UserEntity user, Pageable pageable) {
        return tripRepository.findAllTripByUserId(user.getId(), pageable).map(tripMapper::toResponseDto);
    }

    @Transactional
    public TripDto.TripResponseDto updateTrip(UserEntity user, TripDto.TripPatchDto tripPatchDto) {
        TripEntity trip = tripRepository.findById(tripPatchDto.getTripId()).orElseThrow();
        checkValid(user.getId(), trip.getId());
        tripMapper.updateFromPatchDto(tripPatchDto, trip);
        return tripMapper.toResponseDto(tripRepository.findById(tripPatchDto.getTripId()).orElseThrow());
    }

    @Transactional
    public String deleteTrip(UserEntity user, Long tripId) {
        if (!tripRepository.existsById(tripId)) return "tripId: "+tripId+" 존재하지 않는 trip입니다.";
        checkValid(user.getId(),tripId);
        tripRepository.deleteById(tripId);
        return "tripId: " + tripId + " 삭제 완료";
    }

    private void checkValid(Long userId, Long tripId) {
        if(!tripUserRepository.existsByUserIdAndTripId(userId,tripId)) throw new RuntimeException("Permission denied");
    }
}
