CREATE TABLE formularfeld
(
  id            INTEGER AUTO_INCREMENT,
  formular      integer,
  name          VARCHAR(20),
  x             double,
  y             double,
  font          VARCHAR(20),
  fontsize      integer,
  UNIQUE        (id),
  PRIMARY KEY   (id)
) TYPE=InnoDB;

ALTER TABLE formularfeld ADD CONSTRAINT fkFormularfeld1 FOREIGN KEY (formular) REFERENCES formular (id) ON DELETE CASCADE ON UPDATE CASCADE;
