package com.anify.backend.repository;

import com.anify.backend.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    @Query("SELECT s FROM Series s JOIN UserSeries us ON s.id = us.series.id WHERE us.user.id = :userId AND us.isActive = true")
    List<Series> findByUserId(@Param("userId") Long userId);

    @Query("SELECT s FROM Series s JOIN UserSeries us ON s.id = us.series.id WHERE us.user.id = :userId AND s.name = :name AND us.isActive = true")
    Series findByNameAndUserId(@Param("name") String name, @Param("userId") Long userId);
} 