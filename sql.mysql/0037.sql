create table lehrgangsart
(
  id            INTEGER AUTO_INCREMENT,
  bezeichnung   VARCHAR(50) NOT NULL,
  von           DATE,
  bis           DATE,
  veranstalter  VARCHAR(50),
  UNIQUE        (id),
  PRIMARY KEY   (id)
);
  