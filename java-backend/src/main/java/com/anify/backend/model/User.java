package com.anify.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String spotifyAccessToken;
    private String spotifyRefreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Series> series;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Song> songs;
} 