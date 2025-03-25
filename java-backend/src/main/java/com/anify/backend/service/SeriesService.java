package com.anify.backend.service;

import com.anify.backend.model.Series;
import com.anify.backend.model.User;
import com.anify.backend.repository.SeriesRepository;
import com.anify.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    public Series createSeries(Long userId, Series series) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        series.setUser(user);
        return seriesRepository.save(series);
    }

    public List<Series> getAllSeries() {
        return seriesRepository.findAll();
    }

    public Series getSeriesById(Long id) {
        return seriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Series not found"));
    }

    public Series updateSeries(Long id, Series seriesDetails) {
        Series series = getSeriesById(id);
        series.setName(seriesDetails.getName());
        return seriesRepository.save(series);
    }

    public void deleteSeries(Long id) {
        seriesRepository.deleteById(id);
    }

    public List<Series> getSeriesByUserId(Long userId) {
        return seriesRepository.findByUserId(userId);
    }
} 