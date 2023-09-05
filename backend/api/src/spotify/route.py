from flask import Blueprint, request, jsonify, make_response
from spotify.helpers import create_spotify_playlist, get_spotify_playlists

spotify_bp = Blueprint("spotify", __name__, url_prefix="/spotify")


@spotify_bp.route("/playlist/create", methods=["POST"])
def create_playlist():
    playlistName = request.args.get("playlistName")
    username = request.args.get("username")
    url = create_spotify_playlist(playlistName, username)
    response = make_response(
        jsonify(
            {
                "message": "Created/edited playlist called {0} for {1}: {2}".format(
                    playlistName, username, url
                ),
                "url": url,
            }
        )
    )
    response.headers.add("Access-Control-Allow-Origin", "*")
    return response


@spotify_bp.route("/playlist", methods=["GET"])
def get_playlists():

    playlists = get_spotify_playlists()
    response = make_response(
        jsonify(
            {
                "message": "Retrieved playlists",
                "playlists": playlists,
            }
        )
    )
    response.headers.add("Access-Control-Allow-Origin", "*")
    return response
