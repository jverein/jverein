ALTER TABLE `mitglied` ADD COLUMN externemitgliedsnummer int after id;
CREATE UNIQUE INDEX mitglied_1 on mitglied(externemitgliedsnummer);