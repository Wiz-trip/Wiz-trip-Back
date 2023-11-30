package com.wiztrip.repository;

import com.wiztrip.domain.LandmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandmarkLikeRepository extends JpaRepository<LandmarkEntity,Long> {

}
