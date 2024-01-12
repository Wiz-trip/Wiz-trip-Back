package com.wiztrip.repository;

import com.wiztrip.domain.TripUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripUrlRepository extends JpaRepository<TripUrlEntity, Long> {
    boolean existsByUrl(String Url);
}
