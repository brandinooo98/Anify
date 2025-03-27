import React, { useState } from 'react';
import { useAnilist } from '../../hooks/useAnilist';

interface AnilistFormProps {
  setUsername: (username: string) => void;
  loadAnilistData: () => void;
  isLoading: boolean;
  error: string | null;
  data: any[];
}

export default function AnilistForm({
  setUsername,
  loadAnilistData,
  isLoading,
  error,
  data,
}: AnilistFormProps) {
  const [inputUsername, setInputUsername] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setUsername(inputUsername);
    loadAnilistData();
  };

  return (
    <div className="space-y-8">
      <div className="text-center">
        <h2 className="text-2xl font-bold text-gray-800 mb-2">Connect Your AniList Account</h2>
        <p className="text-gray-600">
          Enter your AniList username to import your anime collection.
        </p>
      </div>

      <form onSubmit={handleSubmit} className="max-w-md mx-auto space-y-4">
        <div>
          <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-1">
            AniList Username
          </label>
          <div className="flex gap-2">
            <input
              type="text"
              id="username"
              value={inputUsername}
              onChange={e => setInputUsername(e.target.value)}
              placeholder="Enter your AniList username"
              className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-colors duration-200"
              required
            />
            <button
              type="submit"
              disabled={isLoading}
              className="px-6 py-2 bg-blue-500 text-white font-semibold rounded-lg hover:bg-blue-600 transition-colors duration-200 disabled:bg-gray-400 disabled:cursor-not-allowed flex items-center"
            >
              {isLoading ? (
                <>
                  <svg
                    className="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
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
                  Loading...
                </>
              ) : (
                'Import'
              )}
            </button>
          </div>
        </div>

        {error && <div className="p-4 bg-red-50 text-red-700 rounded-lg">{error}</div>}

        {data && data.length > 0 && (
          <div className="p-4 bg-green-50 text-green-700 rounded-lg">
            Successfully imported {data.length} anime from your AniList account!
          </div>
        )}
      </form>
    </div>
  );
}
