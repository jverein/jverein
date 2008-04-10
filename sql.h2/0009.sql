create table zusatzfelder
(
  id             IDENTITY,
  mitglied       integer NOT NULL,
  felddefinition integer NOT NULL,
  feld           varchar(1000),
  UNIQUE        (id),
  PRIMARY KEY   (id)
);
ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) DEFERRABLE;
ALTER TABLE zusatzfelder ADD CONSTRAINT fkZusatzfelder2 FOREIGN KEY (felddefinition) REFERENCES felddefinition(id) ON DELETE CASCADE DEFERRABLE;