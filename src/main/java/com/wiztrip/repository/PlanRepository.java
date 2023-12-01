package com.wiztrip.repository;

import com.wiztrip.domain.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity,Long> {
    List<PlanEntity> findAllByTripId(Long tripId);
}
