ALTER TABLE `mitglied` add COLUMN externemitgliedsnummer INTEGER before anrede;
CREATE UNIQUE INDEX mitglied_1 on mitglied(externemitgliedsnummer);