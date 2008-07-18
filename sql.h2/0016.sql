CREATE TABLE formular
(
  id            IDENTITY,
  inhalt        BLOB,
  art           integer,
  bezeichnung   VARCHAR(50),
  UNIQUE        (id),
  PRIMARY KEY   (id)
);
