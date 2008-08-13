CREATE TABLE abrechnung 
(
  id            IDENTITY, 
  mitglied      INTEGER,
  datum         DATE,
  zweck1        VARCHAR(27),
  zweck2        VARCHAR(27),
  betrag        DOUBLE,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
);
