CREATE TABLE konto
(
  id            IDENTITY,
  nummer        VARCHAR(10),
  bezeichnung   VARCHAR(30),
  aufloesung    DATE,
  hibiscusid    INTEGER,
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
);
