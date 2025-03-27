package com.anify.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
@Data
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Song> songs;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserSeries> userSeries;
} 