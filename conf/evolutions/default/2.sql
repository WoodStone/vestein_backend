# Posts schema

# --- !Ups
CREATE TABLE Posts (
  id integer PRIMARY KEY ,
  username text NOT NULL,
  header text NOT NULL,
  content text NOT NULL
);

INSERT INTO Posts(username, header, content) VALUES('vestein', 'Post 1', 'Lorem ipsum 123');
INSERT INTO Posts(username, header, content) VALUES('vestein', 'Post 2', 'Lorem dbium');
INSERT INTO Posts(username, header, content) VALUES('vestein', 'Post 3', 'Lorem pizzaum');
INSERT INTO Posts(username, header, content) VALUES('vestein', 'Post 4', 'Lorem potatum');

# --- !Downs
DROP TABLE Posts;