package com.wiztrip.repository;

import com.wiztrip.domain.LandmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandmarkRepository extends JpaRepository<LandmarkEntity,Long> {

}
