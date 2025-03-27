import spotipy
from spotipy.oauth2 import SpotifyOAuth

# import requests
# from bs4 import BeautifulSoup
# from pyppeteer import launch
# import asyncio
# import regex

# import os
# from dotenv import load_dotenv


def get_spotify_playlists():
    sp = spotipy.Spotify(auth_manager=SpotifyOAuth(scope="playlist-read-private"))
    user_id = sp.me()["id"]
    playlists = sp.user_playlists(user_id)
    ret = []
    for playlist in playlists["items"]:
        ret.append(
            {"name": playlist["name"], "url": playlist["external_urls"]["spotify"]}
        )
    return ret


def create_spotify_playlist(playlistName, username):
    songUris = get_uris(username)
    splitUris = list(divide_chunks(list(songUris), 100))
    sp = spotipy.Spotify(auth_manager=SpotifyOAuth(scope="playlist-modify-public"))
    user_id = sp.me()["id"]
    playlists = sp.user_playlists(user_id)

    pl = None
    for playlist in playlists["items"]:
        if playlist["name"] == playlistName:
            pl = playlist
            break
    if pl == None:
        pl = sp.user_playlist_create(user_id, playlistName)
    populate_playlist(pl["id"], sp, splitUris)
    print(pl)
    return pl["external_urls"]["spotify"]


def populate_playlist(playlist, sp, splitUris):
    length = sp.playlist(playlist)["tracks"]["total"]
    length = int(length / 100) + (length % 100 > 0)
    if length == 0:
        for arr in splitUris:
            sp.playlist_add_items(playlist, arr)
    else:
        for arr in splitUris:
            for song in arr:
                if not is_duplicate(song, playlist, sp, length):
                    sp.playlist_add_items(playlist, [song])


def is_duplicate(song, playlist, sp, length):
    for i in range(length + 1):
        for uri in sp.playlist_tracks(playlist, offset=i * 100)["items"]["track"][
            "uri"
        ]:
            print("URI: {0}".format(uri))
            if uri == song:
                return True
    return False


def get_uris(username):
    from user.song import Song
    from user.series import Series
    from database import db

    songs = []
    series_query = db.session.query(Series.name).filter_by(user=username).all()
    for series in series_query:
        song_query = db.session.query(Song.uri).filter_by(series=series.name).all()
        for song in song_query:
            songs.append(song.uri)
    return songs


def divide_chunks(l, n):
    for i in range(0, len(l), n):
        yield l[i : i + n]


# def extract_list(username):
#     results = []
#     page = 0

#     while True:
#         query = """
#         query ($userName: String, $status_in: [MediaListStatus], $page: Int, $perPage: Int, $type: MediaType) {
# 			Page (page: $page, perPage: $perPage){
# 				mediaList (userName: $userName, status_in: $status_in, type: $type) {
# 					media {
# 						title {
# 							romaji
# 						}
# 					}
# 				}
# 			}
# 		}
#         """
#         variables = {
#             # TODO: Change the string below to your usename
#             "userName": username,
#             "type": "ANIME",
#             "status_in": ["CURRENT", "COMPLETED", "DROPPED"],
#             "page": page,
#             "perPage": 50,
#         }
#         result = requests.post(
#             "https://graphql.anilist.co", json={"query": query, "variables": variables}
#         )
#         if result.json()["data"]["Page"]["mediaList"] == []:
#             break

#         results.append(result.json())
#         page += 1

#     ret = []
#     for dict in results:
#         for entry in dict["data"]["Page"]["mediaList"]:
#             show = regex.sub(r"[^\w\s]", "", entry["media"]["title"]["romaji"]).replace(
#                 " ", "-"
#             )
#             if show not in ret:
#                 ret.append(show)
#     return ret


# async def parse(query_list):
#     browser = await launch(
#         defaultViewPort=None,
#         handleSIGINT=False,
#         handleSIGTERM=False,
#         handleSIGHUP=False,
#         headless=True,
#         args=["--no-sandbox", "--disable-setuid-sandbox"],
#     )
#     page = await browser.newPage()
#     songs = set()
#     for query in query_list:
#         await page.goto("https://aniplaylist.com/{}".format(query), timeout=1000000)
#         print("https://aniplaylist.com/{}".format(query))
#         try:
#             # Agree button
#             await page.click(
#                 "#qc-cmp2-ui > div.qc-cmp2-footer.qc-cmp2-footer-overlay.qc-cmp2-footer-scrolled > div > button.css-1rr34en"
#             )
#         except:  # Sometimes the message doesn't show up
#             pass
#         soup = BeautifulSoup(await page.content(), "html.parser")
#         for link in soup.find_all(href=True):
#             href = link.get("href")
#             if "https://open.spotify.com/track/" in href:
#                 href = href.split("track/")[1].split("?")[0]
#                 href = "spotify:track:" + href
#                 songs.add(href)
#     await browser.close()
#     return songs


# def run_spotify(username, playlistName, songUris, splitUris):
#     # load_dotenv()
#     # for var in ["SPOTIPY_CLIENT_ID", "SPOTIPY_CLIENT_SECRET", "SPOTIPY_REDIRECT_URI"]:
#     #     os.environ[var] = os.getenv(var)
#     loop = asyncio.new_event_loop()
#     asyncio.set_event_loop(loop)
#     loop.run_until_complete(
#         create_spotify_playlist(username, playlistName, songUris, splitUris)
#     )
