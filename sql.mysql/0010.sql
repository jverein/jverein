CREATE TABLE buchungsart
(
  id            INTEGER AUTO_INCREMENT,
  nummer        INTEGER,
  bezeichnung   VARCHAR(30),
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
) TYPE=InnoDB;;
