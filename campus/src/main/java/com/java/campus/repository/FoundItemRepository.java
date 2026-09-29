package com.java.campus.repository;

import com.java.campus.model.FoundItem;
import com.java.campus.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
    List<FoundItem> findByStatus(ItemStatus status);
    List<FoundItem> findByCategoryId(Long categoryId);
    List<FoundItem> findByTitleContainingIgnoreCase(String keyword);
    List<FoundItem> findByCategoryIdAndTitleContainingIgnoreCase(Long categoryId, String keyword);
}