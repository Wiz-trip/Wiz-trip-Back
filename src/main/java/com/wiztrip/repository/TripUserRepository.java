package com.wiztrip.repository;

import com.wiztrip.domain.TripUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripUserRepository extends JpaRepository<TripUserEntity,Long> {
    boolean existsByUserIdAndTripId(Long userId, Long tripId);
}
