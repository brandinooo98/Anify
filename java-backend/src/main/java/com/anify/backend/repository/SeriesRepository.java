package com.anify.backend.repository;

import com.anify.backend.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    List<Series> findByUserId(Long userId);
} 