package com.anify.backend.controller;

import com.anify.backend.model.Series;
import com.anify.backend.model.User;
import com.anify.backend.service.SeriesService;
import com.anify.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
public class SeriesController {
    @Autowired
    private SeriesService seriesService;

    @Autowired
    private UserService userService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<Series> createSeries(@PathVariable Long userId, @RequestBody Series series) {
        User user = userService.getUserById(userId);
        Series createdSeries = seriesService.createSeries(series.getName());
        seriesService.addSeriesToUser(createdSeries, user);
        return ResponseEntity.ok(createdSeries);
    }

    @PostMapping("/{seriesId}/users/{userId}")
    public ResponseEntity<Void> addSeriesToUser(@PathVariable Long seriesId, @PathVariable Long userId) {
        Series series = seriesService.getSeriesById(seriesId);
        User user = userService.getUserById(userId);
        seriesService.addSeriesToUser(series, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{seriesId}/users/{userId}")
    public ResponseEntity<Void> removeSeriesFromUser(@PathVariable Long seriesId, @PathVariable Long userId) {
        seriesService.removeSeriesFromUser(userId, seriesId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Series>> getAllSeries() {
        return ResponseEntity.ok(seriesService.getAllSeries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Series> getSeriesById(@PathVariable Long id) {
        return ResponseEntity.ok(seriesService.getSeriesById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeries(@PathVariable Long id) {
        seriesService.deleteSeries(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Series>> getSeriesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserSeries(userId));
    }
} 