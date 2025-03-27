import React from 'react';
import './App.css';
import AnilistForm from './components/AnilistForm/AnilistForm';
import SpotifyForm from './components/SpotifyForm/SpotifyForm';
import { useAnilist } from './hooks/useAnilist';

function App() {
  const { data, setUsername, loadAnilistData, isLoading, error, username } = useAnilist();

  return (
    <div className="min-h-screen bg-gradient-to-b from-gray-50 to-gray-100">
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 py-6 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between">
            <h1 className="text-3xl font-bold text-gray-900">Anify</h1>
            <p className="text-sm text-gray-500">
              Create Spotify playlists from your AniList favorites
            </p>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 py-8 sm:px-6 lg:px-8">
        <div className="bg-white rounded-lg shadow-lg p-6">
          <AnilistForm
            setUsername={setUsername}
            loadAnilistData={loadAnilistData}
            isLoading={isLoading}
            error={error}
            data={data}
          />
        </div>
        {data && data.length > 0 && (
          <div className="mt-8">
            <div className="bg-white rounded-lg shadow-lg p-6">
              <SpotifyForm username={username} />
            </div>
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
