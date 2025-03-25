package com.anify.backend.service;

import com.anify.backend.model.Song;
import com.anify.backend.model.User;
import com.anify.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpotifyService {
    @Autowired
    private SpotifyApi spotifyApi;

    @Autowired
    private UserRepository userRepository;

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.redirect.uri}")
    private String redirectUri;

    public String getAuthorizationUrl() {
        return spotifyApi.authorizationCodeUri()
                .scope("user-read-private user-read-email playlist-modify-public playlist-modify-private")
                .show_dialog(true)
                .build()
                .execute()
                .toString();
    }

    public void handleCallback(String code) {
        try {
            var authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
            var authorizationCodeResponse = authorizationCodeRequest.execute();

            spotifyApi.setAccessToken(authorizationCodeResponse.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeResponse.getRefreshToken());
        } catch (Exception e) {
            throw new RuntimeException("Failed to handle Spotify callback", e);
        }
    }

    public Playlist createPlaylist(Long userId, String name, List<Song> songs) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            spotifyApi.setAccessToken(user.getSpotifyAccessToken());
            String spotifyUserId = spotifyApi.getCurrentUsersProfile().build().execute().getId();

            CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(spotifyUserId, name)
                    .build();

            Playlist playlist = createPlaylistRequest.execute();

            List<String> trackUris = new ArrayList<>();
            for (Song song : songs) {
                if (song.getSpotifyId() != null) {
                    trackUris.add("spotify:track:" + song.getSpotifyId());
                }
            }

            if (!trackUris.isEmpty()) {
                spotifyApi.addItemsToPlaylist(playlist.getId(), trackUris.toArray(new String[0]))
                        .build()
                        .execute();
            }

            return playlist;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create playlist", e);
        }
    }
} 