package com.java.campus.repository;

import com.java.campus.model.FoundItem;
import com.java.campus.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
    List<FoundItem> findByStatus(ItemStatus status);
    List<FoundItem> findByCategoryId(Long categoryId);
    long countByStatus(ItemStatus status);

    @Query("SELECT f FROM FoundItem f WHERE (:categoryId IS NULL OR f.category.id = :categoryId) AND (:keyword IS NULL OR :keyword = '' OR LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(f.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<FoundItem> findMatchingFoundItems(@Param("categoryId") Long categoryId, @Param("keyword") String keyword);
}