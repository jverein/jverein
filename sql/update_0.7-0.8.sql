ALTER CREATE TABLE zusatzabbuchung
(
  id            INTEGER default UNIQUEKEY('zusatzabbuchung'),
  mitglied      INTEGER NOT NULL,
  faelligkeit   DATE NOT NULL,
  buchungstext  VARCHAR(27) NOT NULL,
  betrag        DOUBLE NOT NULL,
  startdatum    DATE,
  intervall     INTEGER,
  endedatum     DATE,
  ausfuehrung   DATE,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);

UPDATE zusatzabbuchung SET startdatum = faelligkeit where startdatum is null;
UPDATE zusatzabbuchung SET intervall = 0 where intervall is null;

