CREATE TABLE konto
(
  id            INTEGER AUTO_INCREMENT,
  nummer        VARCHAR(10),
  bezeichnung   VARCHAR(30),
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
);
