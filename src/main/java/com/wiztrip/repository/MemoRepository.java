package com.wiztrip.repository;

import com.wiztrip.constant.Category;
import com.wiztrip.domain.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<MemoEntity, Long> {
    List<MemoEntity> findAllByTripIdAndCategory(Long tripId, Category category);
}
