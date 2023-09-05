from flask import Blueprint, request, jsonify, make_response
import user.helpers as h

user_bp = Blueprint("user", __name__, url_prefix="/user")

# TODO: Condense endpoints


@user_bp.route("/init", methods=["POST"])
def create_table():
    h.create_table()
    response = make_response(jsonify({"message": "Created table"}))
    response.headers.add("Access-Control-Allow-Origin", "*")
    return response


@user_bp.route("/clear", methods=["DELETE"])
def clear_tables():
    # TODO: Make this do specifc tables
    h.clear_tables()
    response = make_response(jsonify({"message": "Cleared tables"}))
    response.headers.add("Access-Control-Allow-Origin", "*")
    return response


@user_bp.route("/create", methods=["POST"])
def create_user():
    # TODO: Make users useful?
    username = request.args.get("username")
    h.create_user(username)
    response = make_response(jsonify({"message": "Created user: {}".format(username)}))
    response.headers.add("Access-Control-Allow-Origin", "*")
    return response


@user_bp.route("/delete", methods=["DELETE"])
def delete_user():
    username = request.args.get("username")
    h.delete_user(username)
    response = make_response(jsonify({"message": "Deleted user: {}".format(username)}))
    response.headers.add("Access-Control-Allow-Origin", "*")
    return response


@user_bp.route("/load", methods=["POST"])
def create_series():
    username = request.args.get("username")
    h.create_series(username=username)
    response = make_response(
        jsonify({"message": "Loaded songs for {}".format(username)})
    )
    response.headers.add("Access-Control-Allow-Origin", "*")
    return response


@user_bp.route("/songs", methods=["GET"])
def get_songs():
    series = request.args.get("series")
    songs = h.get_songs(series)
    response = make_response(jsonify({"message": songs}))
    response.headers.add("Access-Control-Allow-Origin", "*")
    return response


@user_bp.route("/", methods=["GET"])
def get_user():
    # TODO: Don't return all users, but something more useful
    username = request.args.get("username")
    user = h.get_user(username) if username else h.get_all_users()
    response = make_response(jsonify({"message": user}))
    response.headers.add("Access-Control-Allow-Origin", "*")
    return response
