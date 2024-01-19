package com.wiztrip.repository;

import com.wiztrip.domain.LandmarkImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandmarkImageRepository extends JpaRepository<LandmarkImageEntity,Long> {
}
