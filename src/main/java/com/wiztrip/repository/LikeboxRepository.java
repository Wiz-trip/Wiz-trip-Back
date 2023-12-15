package com.wiztrip.repository;

import com.wiztrip.domain.LikeboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LikeboxRepository extends JpaRepository<LikeboxEntity,Long> {

    Optional<LikeboxEntity> findByUserId(Long userId);
}
