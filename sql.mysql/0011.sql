CREATE TABLE konto
(
  id            INTEGER AUTO_INCREMENT,
  nummer        VARCHAR(10),
  bezeichnung   VARCHAR(30),
  eroeffnung    DATE,
  aufloesung    DATE,
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
) TYPE=InnoDB;
