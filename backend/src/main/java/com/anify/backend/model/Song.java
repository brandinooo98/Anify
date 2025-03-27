package com.anify.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(nullable = false)
    private String uri;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "series_id")
    @JsonBackReference
    private Series series;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
} 