create table lehrgang
(
  id            IDENTITY,
  mitglied      INTEGER NOT NULL,
  lehrgangsart  INTEGER NOT NULL,
  von           DATE,
  bis           DATE,
  veranstalter  VARCHAR(50),
  ergebnis      VARCHAR(50),
  UNIQUE        (id),
  PRIMARY KEY   (id)
);

  