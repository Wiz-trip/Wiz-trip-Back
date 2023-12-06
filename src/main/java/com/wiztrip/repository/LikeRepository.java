package com.wiztrip.repository;

import com.wiztrip.domain.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LikeRepository extends JpaRepository<LikeEntity,Long> {

    boolean existsByUserId(Long userId);

    Optional<LikeEntity> findByUserId(Long userId);
}
