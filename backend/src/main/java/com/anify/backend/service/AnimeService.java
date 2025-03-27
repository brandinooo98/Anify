package com.anify.backend.service;

import com.anify.backend.model.User;
import com.anify.backend.model.Series;
import com.anify.backend.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class AnimeService {
    private final WebClient webClient;
    private static final String ANILIST_API_URL = "https://graphql.anilist.co";

    @Autowired
    private UserService userService;

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private SeriesRepository seriesRepository;

    public AnimeService() {
        this.webClient = WebClient.builder()
                .baseUrl(ANILIST_API_URL)
                .build();
    }

    public Mono<List<String>> getList(String username) {
        return Mono.fromCallable(() -> {
            // Create or get user first
            User user = userService.createUser(username);
            
            // Get anime list
            List<String> results = new ArrayList<>();
            getAnimeListPage(username, 0)
                .flatMap(response -> {
                    results.addAll(extractAnimeTitles(response));
                    return getRemainingPages(username, 1, results);
                })
                .block(); // Block until complete
            
            // Process results in batches
            int batchSize = 10;
            for (int i = 0; i < results.size(); i += batchSize) {
                int end = Math.min(i + batchSize, results.size());
                List<String> batch = results.subList(i, end);
                
                // Process batch concurrently
                batch.parallelStream().forEach(title -> {
                    try {
                        // Check if series already exists for this user
                        Series existingSeries = seriesRepository.findByNameAndUserId(title, user.getId());
                        if (existingSeries != null) {
                            return; // Skip if series already exists
                        }

                        // Create new series and associate with user
                        Series series = seriesService.createSeries(title);
                        seriesService.addSeriesToUser(series, user);
                        seriesService.scrapeAndCreateSongs(series, user);
                    } catch (Exception e) {
                        // Log error but continue processing other titles
                        System.err.println("Error processing series: " + title + ", Error: " + e.getMessage());
                    }
                });
            }
            
            return results;
        });
    }

    private Mono<List<String>> getRemainingPages(String username, int page, List<String> results) {
        return getAnimeListPage(username, page)
                .flatMap(response -> {
                    List<String> pageResults = extractAnimeTitles(response);
                    if (pageResults.isEmpty()) {
                        return Mono.just(results);
                    }
                    results.addAll(pageResults);
                    return getRemainingPages(username, page + 1, results);
                });
    }

    private Mono<String> getAnimeListPage(String username, int page) {
        String graphqlQuery = """
            query ($userName: String, $status_in: [MediaListStatus], $page: Int, $perPage: Int, $type: MediaType) {
                Page (page: $page, perPage: $perPage){
                    mediaList (userName: $userName, status_in: $status_in, type: $type) {
                        media {
                            title {
                                romaji
                            }
                        }
                    }
                }
            }
            """;

        Map<String, Object> variables = Map.of(
            "userName", username,
            "type", "ANIME",
            "status_in", new String[]{"CURRENT", "COMPLETED", "DROPPED"},
            "page", page,
            "perPage", 50
        );

        Map<String, Object> requestBody = Map.of(
            "query", graphqlQuery,
            "variables", variables
        );

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                    status -> status.is5xxServerError(),
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            System.err.println("AniList API Error Response: " + errorBody);
                            return Mono.error(new RuntimeException("AniList API Error: " + errorBody));
                        })
                )
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("AniList API Response: " + response));
    }

    private List<String> extractAnimeTitles(String response) {
        List<String> titles = new ArrayList<>();
        // Use regex to extract romaji titles from the response
        // This is a simplified version - you might want to use a proper JSON parser
        Pattern pattern = Pattern.compile("\"romaji\":\"([^\"]+)\"");
        java.util.regex.Matcher matcher = pattern.matcher(response);
        while (matcher.find()) {
            String title = matcher.group(1);
            // Clean the title similar to the Python version
            title = title.replaceAll("[^\\w\\s]", "").replaceAll("\\s+", "-");
            if (!titles.contains(title)) {
                titles.add(title);
            }
        }
        return titles;
    }
} 