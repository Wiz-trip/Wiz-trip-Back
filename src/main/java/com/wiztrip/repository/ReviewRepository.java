package com.wiztrip.repository;

import com.wiztrip.domain.ReviewEntity;
import com.wiztrip.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
    Page<ReviewEntity> findByUser(UserEntity user, PageRequest pageRequest);
}
