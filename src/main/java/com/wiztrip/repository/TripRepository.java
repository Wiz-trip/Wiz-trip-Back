package com.wiztrip.repository;

import com.wiztrip.domain.TripEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<TripEntity,Long> {
    @Query("select t from TripEntity t join TripUserEntity tu on tu.trip.id=t.id where tu.user.id=:userId")
    Page<TripEntity> findAllTripByUserId(Long userId, Pageable pageable);

    @Query("select t.id from TripEntity t join TripUserEntity tu on tu.trip.id=t.id where tu.user.id=:userId")
    Page<Long> findAllTripIdByUserId(Long userId, Pageable pageable);

    boolean existsByOwnerIdAndId(Long userId, Long id);

    List<TripEntity> findByFinishedTrue();

    @Query("select count(t.id) from TripEntity t join TripUserEntity tu on tu.trip.id=t.id where tu.user.id=:userId and t.finished=false")
    Integer countByUserIdAndFinishedFalse(Long userId);

}
