from database import db


class Series(db.Model):
    name = db.Column(db.String(64), unique=True, primary_key=True)
    user = db.Column(db.String(64))

    def __repr__(self):
        return "<Series {0} for User {1}>".format(self.name, self.user)
