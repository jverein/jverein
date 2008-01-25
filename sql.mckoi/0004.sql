CREATE TABLE eigenschaften
(
  id            INTEGER	    default UNIQUEKEY('eigenschaften'),
  mitglied      INTEGER     NOT NULL,
  eigenschaft   VARCHAR(50) NOT NULL,
  UNIQUE        (id),
  PRIMARY KEY   (id)
);
