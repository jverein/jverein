CREATE TABLE mitglied 
(
  id            int(10) AUTO_INCREMENT, 
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
)TYPE=InnoDB;

CREATE TABLE beitragsgruppe
(
  id            int(10) AUTO_INCREMENT,
  bezeichnung   VARCHAR(30) NOT NULL,
  betrag		    DOUBLE,
  beitragsart   INTEGER DEFAULT 0,
  UNIQUE        (id),
  PRIMARY KEY   (id)
)TYPE=InnoDB;

ALTER TABLE mitglied ADD CONSTRAINT fkMitglied1 FOREIGN KEY (beitragsgruppe) REFERENCES beitragsgruppe (id) ;

CREATE TABLE zusatzabbuchung
(
  id            int(10) AUTO_INCREMENT,
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
)TYPE=InnoDB;

ALTER TABLE zusatzabbuchung ADD CONSTRAINT fkZusatzabbuchung1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ;

CREATE TABLE stammdaten
(
  id			int(10) AUTO_INCREMENT,
  name			VARCHAR(30) NOT NULL,
  blz			VARCHAR(8)  NOT NULL,
  konto         VARCHAR(10) NOT NULL,
  altersgruppen VARCHAR(50),
  jubilaeen     VARCHAR(50),
  UNIQUE        (id),
  PRIMARY KEY   (id)
)TYPE=InnoDB;

CREATE TABLE kursteilnehmer 
(
  id            int(10) AUTO_INCREMENT,
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
)TYPE=InnoDB;

CREATE TABLE manuellerzahlungseingang 
(
  id            int(10) AUTO_INCREMENT, 
  name          VARCHAR(27) NOT NULL, 
  vzweck1       VARCHAR(27) NOT NULL,
  vzweck2       VARCHAR(27),
  betrag        DOUBLE       NOT NULL,
  eingabedatum  DATE        NOT NULL,
  eingangsdatum DATE,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
)TYPE=InnoDB;

CREATE TABLE wiedervorlage
(
  id            int(10) AUTO_INCREMENT,
  mitglied      INTEGER NOT NULL,
  datum         DATE NOT NULL,
  vermerk       VARCHAR(50) NOT NULL,
  erledigung    DATE,
  UNIQUE        (id),
  PRIMARY KEY   (id)
)TYPE=InnoDB;

ALTER TABLE wiedervorlage ADD CONSTRAINT fkWiedervorlage1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ;

CREATE TABLE eigenschaften
(
  id            int(10) AUTO_INCREMENT,
  mitglied      INTEGER NOT NULL,
  eigenschaft   VARCHAR(50) NOT NULL,
  UNIQUE        (id),
  PRIMARY KEY   (id)
)TYPE=InnoDB;

CREATE UNIQUE INDEX ixEigenschaften1 ON eigenschaften(mitglied, eigenschaft);


CREATE TABLE version
(
  id            int(10) AUTO_INCREMENT, 
  version       INTEGER,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
)TYPE=InnoDB;
INSERT INTO version VALUES (1,5);



