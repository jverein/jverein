CREATE TABLE buchung
(
  id            IDENTITY,
  umsatzid      INTEGER,
  konto         INTEGER      NOT NULL,
  name          VARCHAR(100),
  betrag        DOUBLE       NOT NULL,
  zweck         VARCHAR(35),
  zweck2        VARCHAR(35),
  datum         DATE         NOT NULL,
  art           VARCHAR(100),
  kommentar     LONGVARCHAR,
  buchungsart   INTEGER,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);  

ALTER TABLE buchung ADD CONSTRAINT fkBuchung1 FOREIGN KEY (buchungsart) REFERENCES buchungsart (id) DEFERRABLE;
ALTER TABLE buchung ADD CONSTRAINT fkBuchung2 FOREIGN KEY (konto)       REFERENCES konto (id) DEFERRABLE;
