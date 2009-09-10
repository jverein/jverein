CREATE TABLE buchungsklasse
(
  id            IDENTITY,
  nummer        INTEGER,
  bezeichnung   VARCHAR(30),
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
);
