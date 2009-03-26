CREATE TABLE report
(
  id            IDENTITY,
  bezeichnung   VARCHAR(50),
  art           INTEGER,
  quelle        BLOB,
  kompilat      BLOB,
  aenderung     TIMESTAMP,
  nutzung       TIMESTAMP,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);