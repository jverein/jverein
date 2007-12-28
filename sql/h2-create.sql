CREATE TABLE mitglied 
(
  id            IDENTITY, 
  anrede        VARCHAR(10),
  titel         VARCHAR(10),
  name          VARCHAR(40) NOT NULL, 
  vorname       VARCHAR(40) NOT NULL, 
  strasse       VARCHAR(40) NOT NULL, 
  plz           VARCHAR(5)  NOT NULL, 
  ort           VARCHAR(40) NOT NULL, 
  zahlungsweg   INTEGER,
  zahlungsrhytmus INTEGER,
  blz           VARCHAR(8),
  konto         VARCHAR(10),
  kontoinhaber  VARCHAR(27),
  geburtsdatum  DATE,
  geschlecht    CHAR(1),
  telefonprivat VARCHAR(15),
  telefondienstlich VARCHAR(15),
  email         VARCHAR(50),
  eintritt      DATE,
  beitragsgruppe INTEGER,
  zahlerid      INTEGER,
  austritt      DATE,
  kuendigung    DATE,
  vermerk1      VARCHAR(255),
  vermerk2      VARCHAR(255),
  eingabedatum  DATE,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
);

CREATE TABLE beitragsgruppe
(
  id            IDENTITY,
  bezeichnung   VARCHAR(30) NOT NULL,
  betrag		    DOUBLE,
  beitragsart   INTEGER DEFAULT 0,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);

ALTER TABLE mitglied ADD CONSTRAINT fkMitglied1 FOREIGN KEY (beitragsgruppe) REFERENCES beitragsgruppe (id) DEFERRABLE;

CREATE TABLE zusatzabbuchung
(
  id            IDENTITY,
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

ALTER TABLE zusatzabbuchung ADD CONSTRAINT fkZusatzabbuchung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;

CREATE TABLE stammdaten
(
  id			IDENTITY,
  name			VARCHAR(30) NOT NULL,
  blz			VARCHAR(8)  NOT NULL,
  konto         VARCHAR(10) NOT NULL,
  altersgruppen VARCHAR(50),
  jubilaeen     VARCHAR(50),
  UNIQUE        (id),
  PRIMARY KEY   (id)
);

CREATE TABLE kursteilnehmer 
(
  id            IDENTITY,
  name          VARCHAR(27) NOT NULL, 
  vzweck1       VARCHAR(27) NOT NULL,
  vzweck2       VARCHAR(27),
  blz           VARCHAR(8)  NOT NULL,
  konto         VARCHAR(10) NOT NULL,
  betrag        DOUBLE       NOT NULL,
  geburtsdatum  DATE,
  geschlecht    VARCHAR(1),
  eingabedatum  DATE        NOT NULL,
  abbudatum     DATE,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
);

CREATE TABLE manuellerzahlungseingang 
(
  id            IDENTITY, 
  name          VARCHAR(27) NOT NULL, 
  vzweck1       VARCHAR(27) NOT NULL,
  vzweck2       VARCHAR(27),
  betrag        DOUBLE       NOT NULL,
  eingabedatum  DATE        NOT NULL,
  eingangsdatum DATE,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
);

CREATE TABLE wiedervorlage
(
  id            IDENTITY,
  mitglied      INTEGER NOT NULL,
  datum         DATE NOT NULL,
  vermerk       VARCHAR(50) NOT NULL,
  erledigung    DATE,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);

ALTER TABLE wiedervorlage ADD CONSTRAINT fkWiedervorlage1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;

CREATE TABLE version
(
  id            IDENTITY, 
  version       INTEGER,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
);

COMMIT; 

