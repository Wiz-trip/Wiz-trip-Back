package com.wiztrip.repository;

import com.wiztrip.domain.TripUserEntity;
import com.wiztrip.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripUserRepository extends JpaRepository<TripUserEntity,Long> {
    boolean existsByUserIdAndTripId(Long userId, Long tripId);
    List<TripUserEntity> findByUser(UserEntity user);
}
