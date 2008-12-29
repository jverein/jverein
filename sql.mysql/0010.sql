CREATE TABLE buchungsart
(
  id            INTEGER AUTO_INCREMENT,
  nummer        INTEGER,
  bezeichnung   VARCHAR(30),
  art           INTEGER,
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
) TYPE=InnoDB;;
