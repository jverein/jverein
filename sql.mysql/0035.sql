CREATE TABLE report
(
  id            INTEGER AUTO_INCREMENT,
  bezeichnung   VARCHAR(50),
  art           INTEGER,
  quelle        BLOB,
  kompilat      BLOB,
  aenderung     TIMESTAMP,
  nutzung       TIMESTAMP,
  UNIQUE        (id),
  PRIMARY KEY   (id)
) TYPE=InnoDB;