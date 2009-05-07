ALTER TABLE zusatzfelder ADD CONSTRAINT fkzusatzfelder1 FOREIGN KEY (mitglied) REFERENCES mitglied (id) ON DELETE CASCADE;
