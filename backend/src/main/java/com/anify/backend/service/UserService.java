package com.anify.backend.service;

import com.anify.backend.model.Series;
import com.anify.backend.model.Song;
import com.anify.backend.model.User;
import com.anify.backend.repository.SeriesRepository;
import com.anify.backend.repository.SongRepository;
import com.anify.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SongRepository songRepository;

    public User createUser(String username) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            return existingUser;
        }

        User user = new User();
        user.setUsername(username);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getId();
    }

    public List<Series> getUserSeries(Long userId) {
        return seriesService.getSeriesByUserId(userId);
    }

    @Transactional
    public void createSeriesForUser(Long userId, List<String> shows) {
        User user = getUserById(userId);
        for (String show : shows) {
            // Check if series already exists for this user
            Series existingSeries = seriesRepository.findByNameAndUserId(show, userId);
            if (existingSeries != null) {
                continue; // Skip if series already exists
            }

            // Create new series and associate with user
            Series series = seriesService.createSeries(show);
            seriesService.addSeriesToUser(series, user);
            seriesService.scrapeAndCreateSongs(series, user);
        }
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        return userRepository.save(user);
    }
} 