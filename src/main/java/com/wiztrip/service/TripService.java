package com.wiztrip.service;

import com.wiztrip.domain.TripEntity;
import com.wiztrip.domain.TripUrlEntity;
import com.wiztrip.domain.TripUserEntity;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.ListDto;
import com.wiztrip.dto.TripDto;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.mapstruct.TripMapper;
import com.wiztrip.repository.TripRepository;
import com.wiztrip.repository.TripUrlRepository;
import com.wiztrip.repository.TripUserRepository;
import com.wiztrip.repository.UserRepository;
import com.wiztrip.tool.redis.RedisTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TripService {

    private final TripMapper tripMapper;

    private final TripRepository tripRepository;

    private final TripUserRepository tripUserRepository;

    private final TripUrlRepository tripUrlRepository;

    private final UserRepository userRepository;

    private final RedisTool redisTool;

    @Transactional
    public TripDto.TripResponseDto createTrip(UserEntity user, TripDto.TripPostDto tripPostDto) {
        //본인이 userIdList에 포함되어 있지 않으면 추가
        if(!tripPostDto.getUserIdList().contains(user.getId())) tripPostDto.getUserIdList().add(user.getId());
        return tripMapper.toResponseDto(tripRepository.save(tripMapper.toEntity(user, tripPostDto)));
    }

    @Transactional
    public TripDto.TripUserResponseDto createTripUser(Long tripId, Long userId) {
        TripEntity trip = tripRepository.findById(tripId).orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        checkTripIsFinished(trip);

        if (tripUserRepository.existsByUserIdAndTripId(userId, tripId)) {throw new CustomException(ErrorCode.USER_ALREADY_IN_TRIP);}

        TripUserEntity tripUserEntity = new TripUserEntity();
        tripUserEntity.setTrip(trip);
        tripUserEntity.setUser(user);

        trip.getTripUserEntityList().add(tripUserEntity);

        tripRepository.save(trip);

        return tripMapper.toTripUserResponseDto(trip);
    }

    public TripDto.TripResponseDto getTrip(UserEntity user, Long tripId) {
        TripEntity trip = tripRepository.findById(tripId).orElseThrow(()->new CustomException(ErrorCode.TRIP_NOT_FOUND));
        checkValidByUser(user.getId(), trip.getId());
        return tripMapper.toResponseDto(trip);
    }

    public Page<Long> getTripIdPageByUser(UserEntity user, Pageable pageable) {
        return tripRepository.findAllTripIdByUserId(user.getId(), pageable);
    }

    public Page<TripDto.TripResponseDto> getTripDetailsPageByUser(UserEntity user, Pageable pageable) {
        return tripRepository.findAllTripByUserId(user.getId(), pageable).map(tripMapper::toResponseDto);
    }

    public ListDto<TripDto.TripResponseDto> getTripDetailsByUser(UserEntity user) {
        List<TripUserEntity> tripUserList = tripUserRepository.findByUser(user);

        return new ListDto<>(tripUserList.stream().map(o -> {
            checkValidByUser(user.getId(), o.getTrip().getId());
            return tripMapper.toResponseDto(o.getTrip());
        }).toList());
    }

    public TripDto.MyTripCountResponseDto getMyTripCount(UserEntity user) {
        return tripMapper.toMyCountResponseDto(tripRepository.countByUserIdAndFinishedFalse(user.getId()));
    }

    @Transactional
    public TripDto.TripResponseDto updateTrip(UserEntity user, TripDto.TripPatchDto tripPatchDto) {
        TripEntity trip = tripRepository.findById(tripPatchDto.getTripId()).orElseThrow(()->new CustomException(ErrorCode.TRIP_NOT_FOUND));
        checkTripIsFinished(trip);
        checkValidByUser(user.getId(), trip.getId());
        tripMapper.updateFromPatchDto(tripPatchDto, trip);
        return tripMapper.toResponseDto(trip);
    }

    @Transactional
    public String deleteTrip(UserEntity user, Long tripId) {
        if (!tripRepository.existsById(tripId)) throw new CustomException(ErrorCode.TRIP_NOT_FOUND);
        checkValidByUser(user.getId(),tripId);
        checkValidByOwner(user.getId(),tripId);
        tripRepository.deleteById(tripId);
        return "tripId: " + tripId + " 삭제 완료";
    }

    @Transactional
    public TripDto.TripUrlResponseDto createTripUrl(UserEntity user, Long tripId) {

        TripEntity trip = tripRepository.findById(tripId).orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND));
        checkTripIsFinished(trip);
        checkValidByUser(user.getId(),tripId);

        String url = redisTool.getValues(trip.getId().toString());

        // redis 내에 이전에 생성한 url이 없는 경우, 새로 생성
        if("false".equals(url)) {

            // 랜덤 문자열 생성 (10개 문자)
            do {
                url = java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase().substring(0, 10);
            } while (tripUrlRepository.existsByUrl(url));

            // redis는 key를 통해 value를 찾는 것을 직접적으로 지원하지 않기 때문에 역방향인 경우에서도 저장
            redisTool.setValues(trip.getId().toString(), url);
            redisTool.setValues(url, trip.getId().toString());

            // 유효 기간: 1주일 (7 * 24 * 60 * 60 * 1000)
            redisTool.expireValues(trip.getId().toString(), 7 * 24 * 60 * 60 * 1000);
            redisTool.expireValues(url, 7 * 24 * 60 * 60 * 1000);

            TripUrlEntity tripUrlEntity = new TripUrlEntity();
            tripUrlEntity.setTrip(trip);
            tripUrlEntity.setUrl(url);
            tripUrlRepository.save(tripUrlEntity);

        }

        return tripMapper.toUrlResponseDto(trip);
    }

    public TripDto.TripIdResponseDto getTripUrl(String url) {

        String stringTripId = redisTool.getValues(url);

        if ("false".equals(stringTripId)) {
            if (tripUrlRepository.existsByUrl(url)) {
                throw new CustomException(ErrorCode.EXPIRED_TRIP_URL);
            } else {
                throw new CustomException(ErrorCode.TRIP_URL_NOT_FOUND);
            }
        }

        Long tripId = Long.parseLong(stringTripId);
        TripEntity trip = tripRepository.findById(tripId).orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND));

        return tripMapper.toTripIdResponseDto(trip);
    }

    @Transactional
    public String updateTripFinish(UserEntity user, Long tripId) {
        TripEntity trip = tripRepository.findById(tripId).orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND));
        checkTripIsFinished(trip);
        checkValidByUser(user.getId(),tripId);
        checkValidByOwner(user.getId(),tripId);
        trip.setFinished(true);
        tripRepository.save(trip);
        return "tripId: " + tripId + " 종료 완료";
    }

    private void checkValidByUser(Long userId, Long tripId) {
        if(!tripUserRepository.existsByUserIdAndTripId(userId, tripId)) throw new CustomException(ErrorCode.FORBIDDEN_TRIP_USER);
    }

    // trip owner 확인
    private void checkValidByOwner(Long userId, Long tripId) {
        if(!tripRepository.existsByOwnerIdAndId(userId, tripId)) throw new CustomException(ErrorCode.FORBIDDEN_TRIP_OWNER);
    }

    // trip의 종료 여부 확인
    private void checkTripIsFinished(TripEntity trip) {
        if(trip.isFinished()) {throw new CustomException(ErrorCode.ALREADY_TRIP_FINISHED);}
    }
}
