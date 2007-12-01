CREATE TABLE version
(
  id            INTEGER     default UNIQUEKEY('version'), 
  version     INTEGER,
  UNIQUE        (id), 
  PRIMARY KEY   (id)
);
