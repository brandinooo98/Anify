from database import db


class User(db.Model):
    username = db.Column(db.String(64), index=True, unique=True, primary_key=True)

    def __repr__(self):
        return "<User {}>".format(self.username)
