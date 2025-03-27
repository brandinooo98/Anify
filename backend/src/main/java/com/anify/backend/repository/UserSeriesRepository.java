package com.anify.backend.repository;

import com.anify.backend.model.UserSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserSeriesRepository extends JpaRepository<UserSeries, Long> {
    List<UserSeries> findByUserIdAndIsActiveTrue(Long userId);
    Optional<UserSeries> findByUserIdAndSeriesId(Long userId, Long seriesId);
    boolean existsByUserIdAndSeriesId(Long userId, Long seriesId);
} 