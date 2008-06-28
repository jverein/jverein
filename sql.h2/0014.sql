CREATE TABLE jahresabschluss
(
  id            IDENTITY,
  von           DATE,
  bis           DATE,
  datum         DATE,
  name          VARCHAR(50),
  UNIQUE        (id),
  PRIMARY KEY   (id)
);
