# Import statements have to be inside the functions to prevent circular imports


def create_table():
    from database import db

    db.create_all()


def clear_tables():
    from database import db

    db.drop_all()


def create_user(username):
    from user.user import User
    from database import db

    try:
        user = User(username=username)
        db.session.add(user)
        db.session.commit()
    except:
        db.session.rollback()


def delete_user(username):
    from user.user import User
    from database import db

    try:
        user = User.query.filter_by(username=username).first()
        db.session.delete(user)
        db.session.commit()
    except:
        db.session.rollback()


def create_series(username="", shows=[]):
    import asyncio

    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    loop.run_until_complete(series_runner(username=username, shows=shows))


async def series_runner(username="", shows=[]):
    from user.series import Series
    from user.song import Song
    from database import db

    if shows == [] and username != "":
        shows = get_list(username)
    for name in shows:
        try:
            if (
                db.session.query(Series.name)
                .filter((Series.name == name) & (Series.user == username))
                .first()
                is None
            ):
                series = Series(name=name, user=username)
                db.session.add(series)
                for song in await scrape_songs(name):
                    print(song)
                    if (
                        db.session.query(Song.uri).filter_by(uri=song[0]).first()
                        is None
                    ):
                        song = Song(series=name, uri=song[0], url=song[1])
                        db.session.add(song)
                        db.session.commit()

        except:
            db.session.rollback()


def get_list(username):
    import requests
    import regex

    results = []
    page = 0

    while True:
        query = """
        query ($userName: String, $status_in: [MediaListStatus], $page: Int, $perPage: Int, $type: MediaType) {
			Page (page: $page, perPage: $perPage){
				mediaList (userName: $userName, status_in: $status_in, type: $type) {
					media {
						title {
							romaji
						}
					}
				}
			}
		}
        """
        variables = {
            # TODO: Change the string below to your usename
            "userName": username,
            "type": "ANIME",
            "status_in": ["CURRENT", "COMPLETED", "DROPPED"],
            "page": page,
            "perPage": 50,
        }
        result = requests.post(
            "https://graphql.anilist.co", json={"query": query, "variables": variables}
        )
        if result.json()["data"]["Page"]["mediaList"] == []:
            break

        results.append(result.json())
        page += 1
    ret = []
    for dict in results:
        for entry in dict["data"]["Page"]["mediaList"]:
            show = regex.sub(r"[^\w\s]", "", entry["media"]["title"]["romaji"]).replace(
                " ", "-"
            )
            if show not in ret:
                ret.append(show)
    return ret


async def scrape_songs(show):
    from pyppeteer import launch
    from bs4 import BeautifulSoup

    browser = await launch(
        defaultViewPort=None,
        handleSIGINT=False,
        handleSIGTERM=False,
        handleSIGHUP=False,
        headless=True,
        args=["--no-sandbox", "--disable-setuid-sandbox"],
    )
    page = await browser.newPage()
    songs = set()
    await page.goto("https://aniplaylist.com/{}".format(show), timeout=1000000)
    print("https://aniplaylist.com/{}".format(show))
    try:
        # Agree button
        await page.click(
            "#qc-cmp2-ui > div.qc-cmp2-footer.qc-cmp2-footer-overlay.qc-cmp2-footer-scrolled > div > button.css-1rr34en"
        )
    except:  # Sometimes the message doesn't show up
        pass
    soup = BeautifulSoup(await page.content(), "html.parser")
    for link in soup.find_all(href=True):
        href = link.get("href")
        if "https://open.spotify.com/track/" in href:
            uri = href.split("track/")[1].split("?")[0]
            uri = "spotify:track:" + uri
            songs.add((uri, href))
    await browser.close()
    return songs


def get_songs(series):
    from user.song import Song
    from database import db

    songs = []
    while True:
        query = db.session.query(Song.uri, Song.url).filter_by(series=series).all()
        if query == []:
            create_series(shows=[series])
            continue
        for song in query:
            songs.append({"uri": song.uri, "url": song.url})
        return songs


def get_all_users():
    # from user.user import User

    # query = User.query.all()
    # users = []
    # for user in query:
    #     users.append(user.username)
    # return users
    return "string"


def get_user(username):
    from user.series import Series

    query = Series.query.filter_by(user=username).all()
    users = []
    for user in query:
        users.append({"seriesname": user.name, "username": user.user})
    return users
