package com.wiztrip.repository;

import com.wiztrip.domain.LandmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandmarkRepository extends JpaRepository<LandmarkEntity,Long> {

    @Query("select l from LandmarkLikeboxEntity llb join llb.landmark l where llb.likebox.id = :likeboxId")
    List<LandmarkEntity> findAllByLikeboxId(Long likeboxId);

    @Query("select l.id from LandmarkLikeboxEntity llb join llb.landmark l where llb.likebox.id = :likeboxId")
    List<Long> findAllIdByLikeboxId(Long likeboxId);
}
