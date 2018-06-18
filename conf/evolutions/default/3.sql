# Projects SCHEMA

# --- !Ups
CREATE TABLE Projects(
  id INTEGER PRIMARY KEY,
  header TEXT NOT NULL,
  meta TEXT NOT NULL,
  description TEXT NOT NULL,
  glink TEXT NOT NULL,
  imagelink TEXT NOT NULL
);

INSERT INTO Projects(header, meta, description, glink, imagelink) VALUES('PurpleRain', 'Scala.js', 'Scala.js implementation of the purplerain code challenge.', 'https://github.com/WoodStone/PurpleRain-ScalaJS', '');
INSERT INTO Projects(header, meta, description, glink, imagelink) VALUES('SimpleStackCalc', 'C', 'Simple stack calculator written in C.', 'https://github.com/WoodStone/SimpleStackCalc', '');
INSERT INTO Projects(header, meta, description, glink, imagelink) VALUES('scalatex', 'Scala', 'Scripts for my latex-template.', 'https://github.com/WoodStone/scalatex', '');

# --- !Downs
DROP TABLE Projects