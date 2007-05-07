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


ALTER CREATE TABLE wiedervorlage
(
  id            INTEGER default UNIQUEKEY('wiedervorlage'),
  mitglied      INTEGER NOT NULL,
  datum         DATE NOT NULL,
  vermerk       VARCHAR(50) NOT NULL,
  erledigung    DATE,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);

ALTER TABLE wiedervorlage ADD CONSTRAINT fkWiedervorlage1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;
