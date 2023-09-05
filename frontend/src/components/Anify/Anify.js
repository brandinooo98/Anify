import { useEffect, useState } from "react";
import Loading from "../Loading";
import "./Anify.css";
import PlaylistEmbed from "./Embeds/PlaylistEmbed";
import SongEmbed from "./Embeds/SongEmbed";

const Anify = () => {
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(false);
  const [disabled, setDisabled] = useState(false);
  const [username, setUsername] = useState("");
  const [playlistText, setPlaylistText] = useState("");
  const [playlists, setPlaylists] = useState([]);
  const [series, setSeries] = useState("");
  const [apiResults, setApiResults] = useState({
    message: "",
    songEmbeds: [],
    playlistEmbed: {},
  });

  useEffect(() => {
    fetch(`http://localhost:5000/spotify/playlist`).then((res) =>
      res.json().then((data) => {
        setPlaylists(data.playlists);
      })
    );
  }, []);

  const getSongEmbeds = (results, url) => {
    fetch(`https://open.spotify.com/oembed?url=${url}`).then((res) => {
      res.json().then((data) => {
        results.push(data.html);
      });
    });
  };
  const getPlaylistEmbed = (url) =>
    fetch(`https://open.spotify.com/oembed?url=${url}`).then((res) =>
      res.json().then((data) => data.html)
    );

  const setPlaylist = async (url) => {
    setApiResults({
      message: "Playlist Created",
      songEmbeds: apiResults.songEmbeds,
      playlistEmbed: await getPlaylistEmbed(url),
    });
  };

  const createPlaylist = () => {
    setDisabled(true);
    setLoading(true);
    fetch(
      `http://localhost:5000/spotify/playlist/create?playlistName=${playlistText}&username=${username}`,
      {
        method: "POST",
      }
    )
      .then((res) =>
        res.json().then(async (data) => {
          setLoading(false);
          setDisabled(false);
          setPlaylist(data.url);
        })
      )
      .catch((err) => {
        setLoading(false);
        setDisabled(false);
        setApiResults({
          message: err.message,
          songEmbeds: apiResults.songEmbeds,
          playlistEmbed: {},
        });
      });
  };

  const loadList = () => {
    setDisabled(true);
    setLoading(true);
    fetch(`http://localhost:5000/user/load?username=${username}`, {
      method: "POST",
    })
      .then((res) =>
        res.json().then(() => {
          setLoading(false);
          setDisabled(false);
          setApiResults({
            message: "Anilist Loaded",
            songEmbeds: apiResults.songEmbeds,
            playlistEmbed: apiResults.playlistEmbed,
          });
        })
      )
      .catch((err) => {
        setLoading(false);
        setDisabled(false);
        setApiResults({
          message: err.message,
          songEmbeds: apiResults.songEmbeds,
          playlistEmbed: apiResults.playlistEmbed,
        });
      });
  };

  const searchSeries = () => {
    setDisabled(true);
    setLoading(true);
    fetch(`http://localhost:5000/user/songs?series=${series.replace(" ", "-")}`)
      .then((res) =>
        res.json().then((data) => {
          var arr = [];
          data.message.map((song) => {
            getSongEmbeds(arr, song.url);
          });
          setLoading(false);
          setDisabled(false);
          setApiResults({
            message: `Songs from ${series}`,
            songEmbeds: arr,
            playlistEmbed: apiResults.playlistEmbed,
          });
        })
      )
      .catch((err) => {
        setLoading(false);
        setDisabled(false);
        setApiResults({
          message: err.message,
          songEmbeds: [],
          playlistEmbed: apiResults.playlistEmbed,
        });
      });
  };

  const handleSeries = (event) => {
    setSeries(event.target.value);
  };
  const handleUsername = (event) => {
    setUsername(event.target.value);
  };
  const handlePlaylist = (event) => {
    setPlaylistText(event.target.value);
  };
  const handleLogin = () => {
    setDisabled(false);
    setAuthenticated(true);
  };

  return (
    <div>
      <div className="center">
        {!authenticated ? (
          <div>
            <h1>Username</h1>
            <input type="text" onChange={handleUsername} disabled={disabled} />
            <button className="column" onClick={handleLogin}>
              Log in
            </button>
          </div>
        ) : (
          <div>
            <h1>{`Logged in as ${username}`}</h1>
            <button onClick={loadList} disabled={disabled}>
              Load Anilist
            </button>
            {playlists.length !== 0 && (
              <div>
                <h3>Use a Pre-existing Playlist</h3>
                <fieldset>
                  {playlists.map((playlist) => (
                    <div>
                      <input
                        type="radio"
                        value={playlist.name}
                        onClick={setPlaylist(playlist.url)}
                      />
                      <label>{playlist.name}</label>
                    </div>
                  ))}
                </fieldset>
              </div>
            )}
            <h3>Create a Playlist</h3>
            <div>
              <label>Playlist Name:</label>
              <input
                type="text"
                className="column"
                onChange={handlePlaylist}
                disabled={disabled}
              />
              <button
                className="column"
                onClick={createPlaylist}
                disabled={disabled}
              >
                Create Playlist
              </button>
            </div>
            <h4>Search songs from a specific series</h4>
            <input type="text" onChange={handleSeries} disabled={disabled} />
            <button
              className="column"
              onClick={searchSeries}
              disabled={disabled}
            >
              Search
            </button>
          </div>
        )}
      </div>
      {loading ? (
        <div>
          <Loading />
        </div>
      ) : (
        <div>
          {apiResults.songEmbeds && (
            <div className="center">
              <SongEmbed apiResults={apiResults} />
            </div>
          )}
          {apiResults.playlistEmbed && (
            <div className="center">
              <PlaylistEmbed apiResults={apiResults} />
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default Anify;
