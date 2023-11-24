package com.wiztrip.repository;

import com.wiztrip.domain.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<TripEntity,Long> {
}
