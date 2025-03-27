package com.anify.backend.service;

import com.anify.backend.model.Series;
import com.anify.backend.model.Song;
import com.anify.backend.model.User;
import com.anify.backend.model.UserSeries;
import com.anify.backend.repository.SeriesRepository;
import com.anify.backend.repository.SongRepository;
import com.anify.backend.repository.UserSeriesRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SeriesService {
    private final SeriesRepository seriesRepository;
    private final SongRepository songRepository;
    private final UserSeriesRepository userSeriesRepository;

    public SeriesService(SeriesRepository seriesRepository, 
                        SongRepository songRepository,
                        UserSeriesRepository userSeriesRepository) {
        this.seriesRepository = seriesRepository;
        this.songRepository = songRepository;
        this.userSeriesRepository = userSeriesRepository;
    }

    @Transactional
    public Series createSeries(String name) {
        Series series = new Series();
        series.setName(name);
        return seriesRepository.save(series);
    }

    @Transactional
    public void addSeriesToUser(Series series, User user) {
        if (!userSeriesRepository.existsByUserIdAndSeriesId(user.getId(), series.getId())) {
            UserSeries userSeries = new UserSeries();
            userSeries.setUser(user);
            userSeries.setSeries(series);
            userSeries.setIsActive(true);
            userSeriesRepository.save(userSeries);
        }
    }

    @Transactional
    public void removeSeriesFromUser(Long userId, Long seriesId) {
        userSeriesRepository.findByUserIdAndSeriesId(userId, seriesId)
            .ifPresent(userSeries -> {
                userSeries.setIsActive(false);
                userSeriesRepository.save(userSeries);
            });
    }

    @Transactional
    public void deleteSeries(Long id) {
        seriesRepository.deleteById(id);
    }

    public List<Series> getAllSeries() {
        return seriesRepository.findAll();
    }

    public Series getSeriesById(Long id) {
        return seriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Series not found"));
    }

    public List<Series> getSeriesByUserId(Long userId) {
        return userSeriesRepository.findByUserIdAndIsActiveTrue(userId)
            .stream()
            .map(UserSeries::getSeries)
            .toList();
    }

    @Transactional
    public void scrapeAndCreateSongs(Series series, User user) {
        System.out.println("Scraping songs for series: " + series.getName());
        Set<SongInfo> scrapedSongs = scrapeSongs(series.getName());
        
        for (SongInfo songInfo : scrapedSongs) {
            if (!songRepository.existsByUri(songInfo.getUri())) {
                Song song = new Song();
                song.setUri(songInfo.getUri());
                song.setUrl(songInfo.getUrl());
                song.setSeries(series);
                song.setUser(user);
                song.setTitle("Song from " + series.getName());
                song.setArtist("Various Artists");
                songRepository.save(song);
            }
        }
    }

    private Set<SongInfo> scrapeSongs(String show) {
        Set<SongInfo> songs = new HashSet<>();
        WebDriver driver = null;
        
        try {
            String url = "https://aniplaylist.com/" + show;
            
            // Set up Chrome options
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // Run in headless mode
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            
            // Initialize the driver
            driver = new ChromeDriver(options);
            driver.get(url);
            
            // Wait for the page to load and for links to appear
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("a")));
            
            // Find all links
            List<WebElement> links = driver.findElements(By.tagName("a"));
            
            for (WebElement link : links) {
                String href = link.getAttribute("href");
                if (href != null) {                    
                    // Check for various Spotify URL formats
                    if (href.contains("spotify.com/track/") || 
                        href.contains("spotify:track:") ||
                        href.contains("open.spotify.com/track/")) {
                        
                        String uri;
                        if (href.contains("spotify:track:")) {
                            uri = href;
                        } else {
                            // Extract track ID from URL
                            String trackId = href.split("track/")[1].split("\\?")[0];
                            uri = "spotify:track:" + trackId;
                        }
                        
                        songs.add(new SongInfo(uri, href));
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error scraping songs for " + show + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
        
        return songs;
    }

    private static class SongInfo {
        private final String uri;
        private final String url;
        
        public SongInfo(String uri, String url) {
            this.uri = uri;
            this.url = url;
        }
        
        public String getUri() {
            return uri;
        }
        
        public String getUrl() {
            return url;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SongInfo songInfo = (SongInfo) o;
            return uri.equals(songInfo.uri);
        }
        
        @Override
        public int hashCode() {
            return uri.hashCode();
        }
    }
} 