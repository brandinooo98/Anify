package com.anify.backend.service;

import com.anify.backend.model.Song;
import com.anify.backend.model.User;
import com.anify.backend.repository.SongRepository;
import com.anify.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    public Song createSong(Long userId, Song song) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        song.setUser(user);
        return songRepository.save(song);
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));
    }

    public Song updateSong(Long id, Song songDetails) {
        Song song = getSongById(id);
        song.setTitle(songDetails.getTitle());
        song.setArtist(songDetails.getArtist());
        song.setSpotifyId(songDetails.getSpotifyId());
        return songRepository.save(song);
    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }

    public List<Song> getSongsByUserId(Long userId) {
        return songRepository.findByUserId(userId);
    }

    public List<Song> getSongsBySeriesId(Long seriesId) {
        return songRepository.findBySeriesId(seriesId);
    }
} 