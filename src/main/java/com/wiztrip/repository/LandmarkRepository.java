package com.wiztrip.repository;

import com.wiztrip.domain.LandmarkEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LandmarkRepository extends JpaRepository<LandmarkEntity,Long> {

    @Query("select l.id from LandmarkLikeboxEntity llb join llb.landmark l where llb.likebox.id = :likeboxId")
    List<Long> findAllIdByLikeboxId(Long likeboxId);

    @Query("select l.id from LandmarkLikeboxEntity llb join llb.landmark l where llb.likebox.id = :likeboxId")
    Page<Long> findAllIdByLikeboxId(Long likeboxId, Pageable pageable);

    @Query("select l from LandmarkLikeboxEntity llb join llb.landmark l where llb.likebox.id = :likeboxId")
    List<LandmarkEntity> findAllByLikeboxId(Long likeboxId);

    @Query("select l from LandmarkLikeboxEntity llb join llb.landmark l where llb.likebox.id = :likeboxId")
    Page<LandmarkEntity> findAllByLikeboxId(Long likeboxId, Pageable pageable);

    List<LandmarkEntity> findAllByAreaCode(String areaCode);

    Optional<LandmarkEntity> findByContentId(Long contentId);

    @Query("select l from LandmarkEntity l")
    Page<LandmarkEntity> findAllByPage(Pageable pageable);

    @Query("select l from LandmarkEntity l where l.areaCode=:areaCode")
    Page<LandmarkEntity> findAllByAreaCodeAndPage(String areaCode, Pageable pageable);
}
