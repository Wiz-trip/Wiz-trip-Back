package com.wiztrip.repository;

import com.wiztrip.domain.UserTripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTripRepository extends JpaRepository<UserTripEntity,Long> {

}
