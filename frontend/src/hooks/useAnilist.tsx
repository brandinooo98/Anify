import { useState, useEffect } from 'react';

interface AnimeData {
  title: string;
}

export const useAnilist = () => {
  const [username, setUsername] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<AnimeData[]>([]);

  const loadAnilistData = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch(`http://localhost:8081/api/anime/list?username=${username}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();

      if (Array.isArray(result)) {
        setData(result);
      } else {
        console.error('useAnilist - Response is not an array:', result);
        setData([]);
      }
    } catch (err) {
      console.error('useAnilist - Error:', err);
      setError(err instanceof Error ? err.message : 'Failed to fetch data');
      setData([]);
    } finally {
      setIsLoading(false);
    }
  };

  return { setUsername, loadAnilistData, isLoading, error, data, username };
};
