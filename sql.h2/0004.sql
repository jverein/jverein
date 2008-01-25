CREATE TABLE eigenschaften
(
  id            IDENTITY,
  mitglied      INTEGER NOT NULL,
  eigenschaft   VARCHAR(50) NOT NULL,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);
