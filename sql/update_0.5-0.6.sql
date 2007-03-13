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
  austritt      DATE,
  kuendigung    DATE,
  vermerk1      VARCHAR(255),
  vermerk2      VARCHAR(255),
  eingabedatum  DATE,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
);

UPDATE mitglied set zahlungsweg = 1;

CREATE TABLE manuellerzahlungseingang 
(
  id            INTEGER     default UNIQUEKEY('manuellerzahlungseingang'), 
  name          VARCHAR(27) NOT NULL, 
  vzweck1       VARCHAR(27) NOT NULL,
  vzweck2       VARCHAR(27),
  betrag        DOUBLE       NOT NULL,
  eingabedatum  DATE        NOT NULL,
  eingangsdatum DATE,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
);

