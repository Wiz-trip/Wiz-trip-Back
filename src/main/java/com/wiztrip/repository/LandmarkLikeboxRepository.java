package com.wiztrip.repository;

import com.wiztrip.domain.LandmarkLikeboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LandmarkLikeboxRepository extends JpaRepository<LandmarkLikeboxEntity, Long> {

    boolean existsByLandmarkIdAndLikeboxId(Long likeboxId, Long landmarkId);
}
