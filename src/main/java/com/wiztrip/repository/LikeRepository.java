package com.wiztrip.repository;

import com.wiztrip.domain.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LikeRepository extends JpaRepository<LikeEntity,Long> {

}
