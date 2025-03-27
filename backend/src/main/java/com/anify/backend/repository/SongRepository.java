package com.anify.backend.repository;

import com.anify.backend.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByUserId(Long userId);
    List<Song> findBySeriesId(Long seriesId);
    boolean existsByUri(String uri);
} 