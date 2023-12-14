package com.wiztrip.repository;

import com.wiztrip.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByUsername(String username);

    boolean existsByEmail(String email);
}
