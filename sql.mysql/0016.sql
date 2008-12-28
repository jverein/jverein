CREATE TABLE formular
(
  id            INTEGER AUTO_INCREMENT,
  inhalt        BLOB,
  art           integer,
  bezeichnung   VARCHAR(50),
  UNIQUE        (id),
  PRIMARY KEY   (id)
) TYPE=InnoDB;
