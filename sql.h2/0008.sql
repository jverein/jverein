create table felddefinition
(
  id            IDENTITY,
  name          VARCHAR(50) NOT NULL,
  label         VARCHAR(50) NOT NULL,
  laenge        integer NOT NULL,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);
  