CREATE TABLE buchungsart
(
  id            IDENTITY,
  nummer        INTEGER,
  bezeichnung   VARCHAR(30),
  art           INTEGER,
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
);
