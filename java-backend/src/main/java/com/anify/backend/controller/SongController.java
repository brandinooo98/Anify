package com.anify.backend.controller;

import com.anify.backend.model.Song;
import com.anify.backend.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    @Autowired
    private SongService songService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<Song> createSong(@PathVariable Long userId, @RequestBody Song song) {
        return ResponseEntity.ok(songService.createSong(userId, song));
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        return ResponseEntity.ok(songService.getSongById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @RequestBody Song songDetails) {
        return ResponseEntity.ok(songService.updateSong(id, songDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Song>> getSongsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(songService.getSongsByUserId(userId));
    }

    @GetMapping("/series/{seriesId}")
    public ResponseEntity<List<Song>> getSongsBySeriesId(@PathVariable Long seriesId) {
        return ResponseEntity.ok(songService.getSongsBySeriesId(seriesId));
    }
} 