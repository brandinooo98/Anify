import { useEffect } from 'react';
import { useSpotify } from '../../hooks/useSpotify';
import { useAnilist } from '../../hooks/useAnilist';

interface SpotifyFormProps {
  username: string;
}

export default function SpotifyForm({ username }: SpotifyFormProps) {
  const {
    getSpotifyLoginUrl,
    spotifyLoginUrl,
    spotifyError,
    spotifyLoadingPlaylist,
    spotifyLoadingUrl,
    setSpotifyPlaylistName,
    createSpotifyPlaylist,
    spotifyPlaylistId,
  } = useSpotify();

  useEffect(() => {
    if (!spotifyLoginUrl) {
      getSpotifyLoginUrl();
    }
  }, [getSpotifyLoginUrl, spotifyLoginUrl]);

  const openInNewTab = (url: string) => {
    console.log(`url: ${url}`);
    const newWindow = window.open(url, '_blank', 'noopener,noreferrer');
    if (newWindow) newWindow.opener = null;
  };

  return (
    <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow-lg">
      <div className="space-y-6">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Create Your Spotify Playlist</h2>
          <p className="text-gray-600">
            Connect your Spotify account to create a playlist from your AniList favorites.
          </p>
        </div>

        <div className="flex flex-col items-center space-y-4">
          <button
            onClick={() => openInNewTab(spotifyLoginUrl)}
            disabled={spotifyLoadingUrl}
            className="w-full max-w-md px-6 py-3 bg-green-500 text-white font-semibold rounded-lg hover:bg-green-600 transition-colors duration-200 disabled:bg-gray-400 disabled:cursor-not-allowed flex items-center justify-center"
          >
            {spotifyLoadingUrl ? (
              <>
                <svg
                  className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <circle
                    className="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    strokeWidth="4"
                  ></circle>
                  <path
                    className="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                  ></path>
                </svg>
                Getting Login Data...
              </>
            ) : (
              'Connect to Spotify'
            )}
          </button>

          {spotifyError && (
            <div className="w-full max-w-md p-4 bg-red-50 text-red-700 rounded-lg">
              Error getting login data, please try again.
            </div>
          )}

          <div className="w-full max-w-md space-y-4">
            <p className="text-gray-600 text-center">
              Once you've connected your account, enter a name for your playlist below.
            </p>

            <input
              type="text"
              placeholder="Enter playlist name"
              onChange={e => setSpotifyPlaylistName(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none transition-colors duration-200"
            />

            {spotifyPlaylistId ? (
              <div className="space-y-4">
                <div className="p-4 bg-green-50 text-green-700 rounded-lg text-center">
                  Playlist created successfully!
                </div>
                <div className="flex justify-center">
                  <iframe
                    title="Spotify Playlist"
                    src={`https://open.spotify.com/embed/playlist/${spotifyPlaylistId}`}
                    width="300"
                    height="380"
                    allowTransparency={true}
                    className="rounded-lg shadow-lg"
                  />
                </div>
              </div>
            ) : (
              <button
                onClick={() => createSpotifyPlaylist(username)}
                disabled={spotifyLoadingPlaylist}
                className="w-full px-6 py-3 bg-green-500 text-white font-semibold rounded-lg hover:bg-green-600 transition-colors duration-200 disabled:bg-gray-400 disabled:cursor-not-allowed flex items-center justify-center"
              >
                {spotifyLoadingPlaylist ? (
                  <>
                    <svg
                      className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                    >
                      <circle
                        className="opacity-25"
                        cx="12"
                        cy="12"
                        r="10"
                        stroke="currentColor"
                        strokeWidth="4"
                      ></circle>
                      <path
                        className="opacity-75"
                        fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                      ></path>
                    </svg>
                    Creating Playlist...
                  </>
                ) : (
                  'Create Playlist'
                )}
              </button>
            )}
          </div>

          {spotifyError && (
            <div className="w-full max-w-md p-4 bg-red-50 text-red-700 rounded-lg">
              Error creating playlist, please try again.
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
