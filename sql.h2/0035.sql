CREATE TABLE report
(
  id            IDENTITY,
  bezeichnung   VARCHAR(50),
  quell         BLOB,
  kompilat      BLOB,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);