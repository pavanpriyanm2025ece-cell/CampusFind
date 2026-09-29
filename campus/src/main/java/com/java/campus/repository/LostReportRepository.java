package com.java.campus.repository;

import com.java.campus.model.LostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LostReportRepository extends JpaRepository<LostReport, Long> {
    List<LostReport> findByCategoryId(Long categoryId);
}