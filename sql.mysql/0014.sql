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