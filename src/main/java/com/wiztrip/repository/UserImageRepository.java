package com.wiztrip.repository;

import com.wiztrip.domain.TripUrlEntity;
import com.wiztrip.domain.UserImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImageEntity, Long> {
    Optional<UserImageEntity> findByUserId(Long userId);
}
