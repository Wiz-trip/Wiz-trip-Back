package com.wiztrip.repository;

import com.wiztrip.domain.PlanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity,Long> {
    List<PlanEntity> findAllByTripId(Long tripId);

    @Query("select p.id from PlanEntity p where p.trip.id=:tripId")
    Page<Long> findAllIdByTripId(Long tripId, Pageable pageable);

    @Query("select p from PlanEntity p where p.trip.id=:tripId")
    Page<PlanEntity> findAllByTripId(Long tripId, Pageable pageable);
}
