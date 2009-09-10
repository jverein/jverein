ALTER TABLE `buchungsart` add COLUMN buchungsklasse INTEGER;
CREATE INDEX buchungsart_2 on buchungsart(buchungsklasse);