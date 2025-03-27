import { useState } from 'react';

export const useSpotify = () => {
  const [spotifyLoginUrl, setSpotifyLoginUrl] = useState('');
  const [spotifyError, setSpotifyError] = useState<string | null>(null);
  const [spotifyLoadingUrl, setSpotifyLoadingUrl] = useState(false);
  const [spotifyLoadingPlaylist, setSpotifyLoadingPlaylist] = useState(false);
  const [spotifyPlaylistName, setSpotifyPlaylistName] = useState('');
  const [spotifyPlaylistId, setSpotifyPlaylistId] = useState('');

  const getSpotifyLoginUrl = async () => {
    setSpotifyLoadingUrl(true);
    setSpotifyError(null);
    try {
      const response = await fetch('http://localhost:8081/api/spotify/login');
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      setSpotifyLoginUrl(data.url);
    } catch (err) {
      console.error('Error getting Spotify login URL:', err);
      setSpotifyError(err instanceof Error ? err.message : 'Failed to get login URL');
    } finally {
      setSpotifyLoadingUrl(false);
    }
  };

  const createSpotifyPlaylist = async (username: string) => {
    setSpotifyLoadingPlaylist(true);
    setSpotifyError(null);
    const response = await fetch(
      `http://localhost:8081/api/spotify/playlist?playlistName=${spotifyPlaylistName}&username=${username}`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      }
    )
      .then(res => {
        res.json().then(data => {
          setSpotifyPlaylistId(data.playlistId);
        });
      })
      .catch(err => {
        setSpotifyError(err);
        setSpotifyLoadingPlaylist(false);
      });
    setSpotifyLoadingPlaylist(false);
  };

  return {
    getSpotifyLoginUrl,
    spotifyLoginUrl,
    spotifyError,
    spotifyLoadingUrl,
    spotifyLoadingPlaylist,
    setSpotifyPlaylistName,
    spotifyPlaylistId,
    createSpotifyPlaylist,
  };
};
