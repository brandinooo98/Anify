package com.anify.backend.repository;

import com.anify.backend.model.SpotifyAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpotifyAuthRepository extends JpaRepository<SpotifyAuth, Long> {
    @Query("SELECT s FROM SpotifyAuth s WHERE s.id = :authId")
    SpotifyAuth findAccessTokenByAuthId(@Param("authId") Long authId);

    @Query("SELECT s FROM SpotifyAuth s WHERE s.id = :authId")
    SpotifyAuth findRefreshTokenByAuthId(@Param("authId") Long authId);
}
