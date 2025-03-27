package com.anify.backend.controller;

import com.anify.backend.model.Series;
import com.anify.backend.model.User;
import com.anify.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestParam String username) {
        return userService.createUser(username);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<Long> getUserIdByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserIdByUsername(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/scrape-songs")
    public ResponseEntity<Void> scrapeSongsForUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        List<Series> series = userService.getUserSeries(id);
        userService.createSeriesForUser(id, series.stream()
            .map(Series::getName)
            .toList());
        return ResponseEntity.ok().build();
    }
} 