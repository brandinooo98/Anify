package com.anify.backend.controller;

import com.anify.backend.model.Song;
import com.anify.backend.service.SongService;
import com.anify.backend.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

import java.util.List;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {
    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private SongService songService;

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok(spotifyService.getAuthorizationUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<Void> callback(@RequestParam String code) {
        spotifyService.handleCallback(code);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/playlists")
    public ResponseEntity<Playlist> createPlaylist(@RequestParam Long userId, @RequestParam String name, @RequestParam List<Long> songIds) {
        List<Song> songs = songIds.stream()
                .map(songService::getSongById)
                .toList();
        return ResponseEntity.ok(spotifyService.createPlaylist(userId, name, songs));
    }
} 