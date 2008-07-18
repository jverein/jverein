CREATE TABLE spendenbescheinigung
(
  id            IDENTITY,
  zeile1        VARCHAR(40),
  zeile2        VARCHAR(40),
  zeile3        VARCHAR(40),
  zeile4        VARCHAR(40),
  zeile5        VARCHAR(40),
  zeile6        VARCHAR(40),
  zeile7        VARCHAR(40),
  spendedatum   DATE,
  bescheinigungsdatum DATE,
  betrag        DOUBLE,
  formular      INTEGER,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);

ALTER TABLE spendenbescheinigung ADD CONSTRAINT fkSpendenbescheinigung1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE RESTRICT;
