from flask import Flask
from flask_cors import CORS
from flask_session import Session
from database import db


def register_modules(app):
    from spotify.route import spotify_bp
    from user.route import user_bp

    app.register_blueprint(user_bp)
    app.register_blueprint(spotify_bp)


def init_app():
    from user.song import Song
    from user.series import Series

    app = Flask(__name__)
    cors = CORS(app)

    # app.config["SESSION_TYPE"] = "filesystem"
    # app.config["SESSION_FILE_DIR"] = "./.flask_session/"
    # Session(app)
    app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///database.db"
    app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False

    db.init_app(app)
    register_modules(app)

    return app


def setup_db(app):
    with app.app_context():
        db.create_all()


if __name__ == "__main__":
    app = init_app()
    setup_db(app)
    app.run(host="127.0.0.1", port=5000, debug=True)
