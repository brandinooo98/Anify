package com.anify.backend.controller;

import com.anify.backend.model.Series;
import com.anify.backend.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
public class SeriesController {
    @Autowired
    private SeriesService seriesService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<Series> createSeries(@PathVariable Long userId, @RequestBody Series series) {
        return ResponseEntity.ok(seriesService.createSeries(userId, series));
    }

    @GetMapping
    public ResponseEntity<List<Series>> getAllSeries() {
        return ResponseEntity.ok(seriesService.getAllSeries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Series> getSeriesById(@PathVariable Long id) {
        return ResponseEntity.ok(seriesService.getSeriesById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Series> updateSeries(@PathVariable Long id, @RequestBody Series seriesDetails) {
        return ResponseEntity.ok(seriesService.updateSeries(id, seriesDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeries(@PathVariable Long id) {
        seriesService.deleteSeries(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Series>> getSeriesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(seriesService.getSeriesByUserId(userId));
    }
} 