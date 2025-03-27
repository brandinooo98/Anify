package com.anify.backend.controller;

import com.anify.backend.model.User;
import com.anify.backend.model.Song;
import com.anify.backend.service.SpotifyService;
import com.anify.backend.service.UserService;
import com.anify.backend.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {
    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private UserService userService;

    @Autowired
    private SongService songService;

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> getLoginUrl() {
        return ResponseEntity.ok(Map.of("url", spotifyService.getAuthorizationUrl()));
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam String code) {
        return ResponseEntity.ok(spotifyService.handleCallback(code));
    }

    @PostMapping("/playlist")
    public ResponseEntity<Map<String, String>> createPlaylist(@RequestParam String username, @RequestParam String playlistName) {
        try {
            String playlistId = spotifyService.createPlaylistFromUsername(username, playlistName, Long.valueOf(1));
            return ResponseEntity.ok(Map.of("playlistId", playlistId));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not authenticated")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", e.getMessage()));
            } else if (e.getMessage().contains("No songs found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
} 