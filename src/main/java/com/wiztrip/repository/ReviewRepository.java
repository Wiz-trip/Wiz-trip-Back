package com.wiztrip.repository;

import com.wiztrip.domain.ReviewEntity;
import com.wiztrip.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
    List<ReviewEntity> findByUser(UserEntity user);
}
