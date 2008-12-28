CREATE TABLE buchung
(
  id            INTEGER AUTO_INCREMENT,
  umsatzid      INTEGER,
  konto         INTEGER  NOT NULL,
  name          VARCHAR(100),
  betrag        DOUBLE       NOT NULL,
  zweck         VARCHAR(35),
  zweck2        VARCHAR(35),
  datum         DATE         NOT NULL,
  art           VARCHAR(100),
  kommentar     TEXT,
  buchungsart   INTEGER,
  PRIMARY KEY   (id)
) TYPE=InnoDB;  

ALTER TABLE buchung ADD CONSTRAINT fkBuchung1 FOREIGN KEY (buchungsart) REFERENCES buchungsart (id);
ALTER TABLE buchung ADD CONSTRAINT fkBuchung2 FOREIGN KEY (konto)       REFERENCES konto (id);