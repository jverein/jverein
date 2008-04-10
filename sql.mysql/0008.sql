create table felddefinition
(
  id            int(10) AUTO_INCREMENT,
  name          VARCHAR(50) NOT NULL,
  label         VARCHAR(50) NOT NULL,
  laenge        integer NOT NULL,
  UNIQUE        (id),
  PRIMARY KEY   (id)
)TYPE=InnoDB;
  