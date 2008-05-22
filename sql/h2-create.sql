CREATE TABLE mitglied 
(
  id            IDENTITY, 
  externemitgliedsnummer INTEGER,
  anrede        VARCHAR(10),
  titel         VARCHAR(20),
  name          VARCHAR(40) NOT NULL, 
  vorname       VARCHAR(40) NOT NULL, 
  strasse       VARCHAR(40) NOT NULL, 
  plz           VARCHAR(10)  NOT NULL, 
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
  UNIQUE        (externemitgliedsnummer),
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

CREATE TABLE eigenschaften
(
  id            IDENTITY,
  mitglied      INTEGER NOT NULL,
  eigenschaft   VARCHAR(50) NOT NULL,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);

CREATE UNIQUE INDEX ixEigenschaften1 ON eigenschaften(mitglied, eigenschaft);

CREATE TABLE version
(
  id            IDENTITY, 
  version       INTEGER,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
);

create table felddefinition
(
  id            IDENTITY,
  name          VARCHAR(50) NOT NULL,
  label         VARCHAR(50) NOT NULL,
  laenge        integer NOT NULL,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);
create table zusatzfelder
(
  id             IDENTITY,
  mitglied       integer NOT NULL,
  felddefinition integer NOT NULL,
  feld           varchar(1000),
  UNIQUE        (id),
  PRIMARY KEY   (id)
);
ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;
ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder2 FOREIGN KEY (felddefinition) REFERENCES felddefinition(id) ON DELETE CASCADE DEFERRABLE;


CREATE TABLE konto
(
  id            IDENTITY,
  nummer        VARCHAR(10),
  bezeichnung   VARCHAR(30),
  aufloesung    DATE,
  hibiscusid    INTEGER,
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
);

CREATE TABLE buchungsart
(
  id            IDENTITY,
  nummer        INTEGER,
  bezeichnung   VARCHAR(30),
  buchungsart   INTEGER,
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
);

CREATE TABLE buchung
(
  id            CREATE TABLE konto
(
  id            IDENTITY,
  nummer        VARCHAR(10),
  bezeichnung   VARCHAR(30),
  aufloesung    DATE,
  hibiscusid    INTEGER,
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
);

CREATE TABLE buchungsart
(
  id            IDENTITY,
  nummer        INTEGER,
  bezeichnung   VARCHAR(30),
  buchungsart   INTEGER,
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
);

CREATE TABLE buchung
(
  id            IDENTITY,
  umsatzid      INTEGER,
  konto         INTEGER  NOT NULL,
  name          VARCHAR(100),
  betrag        DOUBLE       NOT NULL,
  zweck         VARCHAR(35),
  zweck2        VARCHAR(35),
  datum         DATE         NOT NULL,
  saldo         DOUBLE       NOT NULL,
  art           VARCHAR(100),
  kommentar     TEXT,
  buchungsart   INTEGER,
  PRIMARY KEY   (id)
);  

ALTER TABLE buchung ADD CONSTRAINT fkBuchung1 FOREIGN KEY (buchungsart) REFERENCES buchungsart (id) DEFERRABLE;
ALTER TABLE buchung ADD CONSTRAINT fkBuchung2 FOREIGN KEY (konto)       REFERENCES konto (id) DEFERRABLE;

CREATE TABLE anfangsbestand
(
  id            IDENTITY,
  konto         INTEGER,
  datum         DATE,
  UNIQUE        (id),
  UNIQUE        (konto, datum),
  PRIMARY KEY   (id)
);

ALTER TABLE anfangsbestand ADD CONSTRAINT fkAnfangsbestand1 FOREIGN KEY (konto) REFERENCES konto (nummer) DEFERRABLE;

,
  umsatzid      INTEGER,
  konto         INTEGER  NOT NULL,
  name          VARCHAR(100),
  betrag        DOUBLE       NOT NULL,
  zweck         VARCHAR(35),
  zweck2        VARCHAR(35),
  datum         DATE         NOT NULL,
  saldo         DOUBLE       NOT NULL,
  art           VARCHAR(100),
  kommentar     TEXT,
  buchungsart   INTEGER,
  PRIMARY KEY   (id)
);  

ALTER TABLE buchung ADD CONSTRAINT fkBuchung1 FOREIGN KEY (buchungsart) REFERENCES buchungsart (id);
ALTER TABLE buchung ADD CONSTRAINT fkBuchung2 FOREIGN KEY (konto)       REFERENCES konto (id);

CREATE TABLE anfangsbestand
(
  id            IDENTITY,
  konto         INTEGER,
  datum         DATE,
  UNIQUE        (id),
  UNIQUE        (konto, datum),
  PRIMARY KEY   (id)
);

ALTER TABLE anfangsbestand ADD CONSTRAINT fkAnfangsbestand1 FOREIGN KEY (konto) REFERENCES konto (nummer) DEFERRABLE;


INSERT INTO version VALUES (1,13);

COMMIT; 

