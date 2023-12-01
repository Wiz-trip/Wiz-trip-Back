package com.wiztrip.service;

import com.wiztrip.domain.TripEntity;
import com.wiztrip.dto.TripDto;
import com.wiztrip.mapstruct.TripMapper;
import com.wiztrip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TripService {

    private final TripMapper tripMapper;

    private final TripRepository tripRepository;

    @Transactional
    public TripDto.TripResponseDto createTrip(TripDto.TripPostDto tripPostDto) {
        return tripMapper.toResponseDto(tripRepository.save(tripMapper.toEntity(tripPostDto)));
    }

    public TripDto.TripResponseDto getTrip(Long tripId) {
        return tripMapper.toResponseDto(tripRepository.findById(tripId).orElseThrow());
    }

    @Transactional
    public TripDto.TripResponseDto updateTrip(TripDto.TripPatchDto tripPatchDto) {
        TripEntity trip = tripRepository.findById(tripPatchDto.getTripId()).orElseThrow();
        tripMapper.updateFromPatchDto(tripPatchDto, trip);
        return tripMapper.toResponseDto(tripRepository.findById(tripPatchDto.getTripId()).orElseThrow());
    }

    @Transactional
    public String deleteTrip(Long tripId) {
        if (!tripRepository.existsById(tripId)) return "tripId: "+tripId+" 존재하지 않는 trip입니다.";
        tripRepository.deleteById(tripId);
        return "tripId: " + tripId + " 삭제 완료";
    }
}
