CREATE TABLE anfangsbestand
(
  id            INTEGER AUTO_INCREMENT,
  konto         INTEGER,
  datum         DATE,
  betrag        DOUBLE,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);

ALTER TABLE anfangsbestand ADD CONSTRAINT fkAnfangsbestand1 FOREIGN KEY (konto) REFERENCES konto (id);
