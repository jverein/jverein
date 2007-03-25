ALTER CREATE TABLE beitragsgruppe
(
  id            INTEGER       default UNIQUEKEY('beitragsgruppe'),
  bezeichnung   VARCHAR(30) NOT NULL,
  betrag		    DOUBLE,
  beitragsart   INTEGER DEFAULT 0,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);

ALTER CREATE TABLE mitglied 
(
  id            INTEGER     default UNIQUEKEY('mitglied'), 
  anrede        VARCHAR(10),
  titel         VARCHAR(10),
  name          VARCHAR(40) NOT NULL, 
  vorname       VARCHAR(40) NOT NULL, 
  strasse       VARCHAR(40) NOT NULL, 
  plz           VARCHAR(5)  NOT NULL, 
  ort           VARCHAR(40) NOT NULL, 
  zahlungsweg   INTEGER,
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