CREATE TABLE abrechnung 
(
  id            IDENTITY, 
  mitgliedsnummer INTEGER,
  datum         DATE,
  zweck1        VARCHAR(27),
  zweck2        VARCHAR(27),
  betrag        DOUBLE,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
);
