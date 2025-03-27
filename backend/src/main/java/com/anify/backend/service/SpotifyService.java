package com.anify.backend.service;

import com.anify.backend.model.User;
import com.anify.backend.model.Song;
import com.anify.backend.model.SpotifyAuth;
import com.anify.backend.repository.UserRepository;
import com.anify.backend.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.model_objects.specification.Track;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import java.util.Arrays;

@Service
public class SpotifyService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongService songService;

    @Autowired
    private UserService userService;

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.redirect.uri}")
    private String redirectUri;

    private SpotifyApi spotifyApi;

    @PostConstruct
    public void init() {
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(URI.create(redirectUri))
                .build();
    }

    public String getAuthorizationUrl() {
        try {
            return spotifyApi.authorizationCodeUri()
                    .scope("playlist-modify-public playlist-modify-private")
                    .build()
                    .execute()
                    .toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get authorization URL: " + e.getMessage());
        }
    }

    public String handleCallback(String code) {
        try {
            AuthorizationCodeCredentials credentials = spotifyApi.authorizationCode(code)
                    .build()
                    .execute();

            spotifyApi.setAccessToken(credentials.getAccessToken());
            spotifyApi.setRefreshToken(credentials.getRefreshToken());

            // Get current user
            SpotifyAuth spotifyAuth = spotifyAuthService.createSpotifyAuth(
                credentials.getAccessToken(),
                credentials.getRefreshToken()
            );

            return "Successfully authenticated with Spotify! You can now close this window.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to authenticate with Spotify: " + e.getMessage();
        }
    }

    public String createPlaylistFromUsername(String username, String name, Long authId) {
        User user = userService.getUserById(userService.getUserIdByUsername(username));
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Get all songs for the user
        List<Song> songs = songService.getSongsByUserId(user.getId());
        if (songs.isEmpty()) {
            throw new RuntimeException("No songs found for user");
        }

        // Create playlist
        String playlistId = createPlaylist(user, name);
        if (playlistId == null) {
            throw new RuntimeException("Failed to create playlist");
        }

        // Add songs to playlist
        addSongsToPlaylist(authId, playlistId, songs);

        return playlistId;
    }

    public String createPlaylist(User user, String name) {
        try {
            String userId = getCurrentUserId(user);
            CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userId, name)
                    .build();

            Playlist playlist = createPlaylistRequest.execute();
            return playlist.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addSongsToPlaylist(Long authId, String playlistId, List<Song> songs) {
        try {
            // Set the access token
            SpotifyAuthService.SpotifyAuthCredentials spotifyAuth = spotifyAuthService.getSpotifyAuthCredentials(authId);
            spotifyApi.setAccessToken(spotifyAuth.getAccessToken());
            
            List<String> uris = songs.stream()
                    .map(Song::getUri)
                    .collect(Collectors.toList());
            
            // Add tracks in batches of 100 (Spotify API limit)
            for (int i = 0; i < uris.size(); i += 20) {
                int end = Math.min(i + 20, uris.size());
                List<String> batch = uris.subList(i, end);
                
                try {
                    String[] urisArray = batch.toArray(new String[0]);
                    
                    // Create and execute the request
                    AddItemsToPlaylistRequest addItemsRequest = spotifyApi.addItemsToPlaylist(playlistId, urisArray)
                            .build();
                    
                    // Wait for the request to complete
                    addItemsRequest.execute();
                } catch (Exception e) {
                    System.err.println("Failed to add batch " + (i/20 + 1) + ": " + e.getMessage());
                    e.printStackTrace();
                    // Continue with next batch instead of throwing
                    continue;
                }
            }
        } catch (Exception e) {
            System.err.println("Error adding songs to playlist: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add songs to playlist: " + e.getMessage());
        }
    }

    private String getCurrentUserId(User user) {
        try {
            return spotifyApi.getCurrentUsersProfile().build().execute().getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get current user ID: " + e.getMessage());
        }
    }
} 