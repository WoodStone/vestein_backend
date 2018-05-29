# User schema

# --- !Ups
CREATE TABLE Users (
  username text PRIMARY KEY,
  pass text NOT NULL,
  email text NOT NULL
);

INSERT INTO Users(username, pass, email) VALUES('vestein', '123', 'vesteindahl.dev@gmail.com');


# --- !Downs
DROP TABLE Users;