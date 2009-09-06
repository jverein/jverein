CREATE TABLE mitglied 
(
  id            int(10) AUTO_INCREMENT, 
  externemitgliedsnummer int(10),
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
  handy         VARCHAR(15),
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
  id            int(10) AUTO_INCREMENT,
  name          VARCHAR(30) NOT NULL,
  blz           VARCHAR(8)  NOT NULL,
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

CREATE TABLE `version` 
(
  `id` INTEGER AUTO_INCREMENT,
  `version`     INTEGER,
  UNIQUE        (`id`),
  PRIMARY KEY   (`id`)
) TYPE=InnoDB;

create table felddefinition
(
  id            int(10) AUTO_INCREMENT,
  name          VARCHAR(50) NOT NULL,
  label         VARCHAR(50) NOT NULL,
  laenge        integer NOT NULL,
  UNIQUE        (id),
  PRIMARY KEY   (id)
)TYPE=InnoDB;
  
create table zusatzfelder
(
  id             integer auto_increment,
  mitglied       integer NOT NULL,
  felddefinition integer NOT NULL,
  feld           varchar(1000),
  UNIQUE        (id),
  PRIMARY KEY   (id)
)TYPE=InnoDB;

ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id);
ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder2 FOREIGN KEY (felddefinition) REFERENCES felddefinition(id) ON DELETE CASCADE;

CREATE TABLE konto
(
  id            INTEGER AUTO_INCREMENT,
  nummer        VARCHAR(10),
  bezeichnung   VARCHAR(30),
  eroeffnung    DATE,
  aufloesung    DATE,
  hibiscusid    INTEGER,
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
) TYPE=InnoDB;

CREATE TABLE buchungsart
(
  id            INTEGER AUTO_INCREMENT,
  nummer        INTEGER,
  bezeichnung   VARCHAR(30),
  art           INTEGER,
  UNIQUE        (id),
  UNIQUE        (nummer),
  PRIMARY KEY   (id)
) TYPE=InnoDB;

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

CREATE TABLE anfangsbestand
(
  id            INTEGER AUTO_INCREMENT,
  konto         INTEGER,
  datum         DATE,
  BETRAG        DOUBLE,
  UNIQUE        (id),
  UNIQUE        (konto, datum),
  PRIMARY KEY   (id)
) TYPE=InnoDB;

ALTER TABLE anfangsbestand ADD CONSTRAINT fkAnfangsbestand1 FOREIGN KEY (konto) REFERENCES konto (id);

CREATE TABLE jahresabschluss
(
  id            INTEGER AUTO_INCREMENT,
  von           DATE,
  bis           DATE,
  datum         DATE,
  name          VARCHAR(50),
  UNIQUE        (id),
  PRIMARY KEY   (id)
) TYPE=InnoDB;

INSERT INTO version VALUES (1,15);



