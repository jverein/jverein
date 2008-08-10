ALTER TABLE abrechnung ADD CONSTRAINT fkAbrechnung1 FOREIGN KEY (mitgliedsnummer) REFERENCES mitglied (id) ON DELETE RESTRICT;
