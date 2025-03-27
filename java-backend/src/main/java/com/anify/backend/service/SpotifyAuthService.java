package com.anify.backend.service;

import com.anify.backend.model.SpotifyAuth;
import com.anify.backend.repository.SpotifyAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpotifyAuthService {
    @Autowired
    private SpotifyAuthRepository spotifyAuthRepository;

    public SpotifyAuth createSpotifyAuth(String accessToken, String refreshToken) {
        SpotifyAuth auth = new SpotifyAuth();
        auth.setAccessToken(accessToken);
        auth.setRefreshToken(refreshToken);
        return spotifyAuthRepository.save(auth);
    }

    public SpotifyAuthCredentials getSpotifyAuthCredentials(Long authId) {
        SpotifyAuth auth = spotifyAuthRepository.findById(authId)
                .orElseThrow(() -> new RuntimeException("SpotifyAuth not found"));
        return new SpotifyAuthCredentials(auth.getAccessToken(), auth.getRefreshToken());
    }

    public static class SpotifyAuthCredentials {
        private String accessToken;
        private String refreshToken;

        public SpotifyAuthCredentials(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }
}