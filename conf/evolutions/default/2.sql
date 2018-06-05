# Posts schema

# --- !Ups
CREATE TABLE Posts (
  id integer PRIMARY KEY,
  username TEXT NOT NULL,
  timestamp TEXT NOT NULL,
  header TEXT NOT NULL,
  content TEXT NOT NULL
);

INSERT INTO Posts(username, timestamp, header, content) VALUES('vestein', 'YYYY-MM-DD HH:MM:SS.SSS', 'Post 1', 'Lorem ipsum 123');
INSERT INTO Posts(username, timestamp, header, content) VALUES('vestein', 'YYYY-MM-DD HH:MM:SS.SSS', 'Post 2', 'Lorem dbium');
INSERT INTO Posts(username, timestamp, header, content) VALUES('vestein', 'YYYY-MM-DD HH:MM:SS.SSS', 'Post 3', 'Lorem pizzaum');
INSERT INTO Posts(username, timestamp, header, content) VALUES('vestein', 'YYYY-MM-DD HH:MM:SS.SSS', 'Post 4', 'Lorem potatum');

# --- !Downs
DROP TABLE Posts;