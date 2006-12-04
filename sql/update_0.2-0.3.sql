------------------------------------------------------------------------
-- $Source$
-- $Revision$
-- $Date$
-- $Author$
--
-- Copyright (c) by Heiner Jostkleigrewe
-- All rights reserved
--
------------------------------------------------------------------------
--
--
ALTER CREATE TABLE stammdaten
(
  id			INTEGER		default UNIQUEKEY('stammdaten'),
  name			VARCHAR(30) NOT NULL,
  blz			VARCHAR(8)  NOT NULL,
  konto         VARCHAR(10) NOT NULL,
  altersgruppen VARCHAR(50),
  UNIQUE        (id),
  PRIMARY KEY   (id)
);

------------------------------------------------------------------------
-- $Log$
-- Revision 1.1  2006/10/29 07:51:28  jost
-- Neu: Mitgliederstatistik
--
------------------------------------------------------------------------