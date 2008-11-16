CREATE TABLE einstellung 
(
  id                       IDENTITY, 
  geburtsdatumpflicht      CHAR(5),
  eintrittsdatumpflicht    CHAR(5),
  kommunikationsdaten      CHAR(5),
  zusatzabbuchung          CHAR(5),
  vermerke                 CHAR(5),
  wiedervorlage            CHAR(5),
  kursteilnehmer           CHAR(5),
  externemitgliedsnummer   CHAR(5),
  beitragsmodel            INTEGER,
  dateinamenmuster         VARCHAR(50),
  beginngeschaeftsjahr     CHAR(6),
  rechnungfuerabbuchung    CHAR(5),
  rechnungfuerueberweisung CHAR(5),
  rechnungfuerbarzahlung   CHAR(5),
  UNIQUE                   (id), 
  PRIMARY KEY              (id)
);
