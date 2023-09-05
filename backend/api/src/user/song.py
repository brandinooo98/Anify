from database import db


class Song(db.Model):
    series = db.Column(db.String(64))
    url = db.Column(db.String(64), unique=True)
    uri = db.Column(db.String(64), unique=True, primary_key=True)

    def __repr__(self):
        return "<The uri for {0} is {1}>".format(self.series, self.uri)
