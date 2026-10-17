-- Database: db_people

CREATE DATABASE db_people;
USE db_people;

CREATE TABLE person (
  id varchar(255) PRIMARY KEY,
  name varchar(255) NOT NULL,
  birth_y int NOT NULL,
  category varchar(255) NOT NULL,
  level int NOT NULL
);

INSERT INTO person (id, name, birth_y, category, level) VALUES
  ('SBR-001', 'Lord Logres', 480, 'Saber', 90),
  ('ARC-001', 'Super Orion', 1200, 'Archer', 89),
  ('LNC-001', 'Romulus-Quirinus', 753, 'Lancer', 75),
  ('RDR-001', 'Noah', 450, 'Rider', 80),
  ('CST-001', 'King Solomon', 970, 'Caster', 90),
  ('ASS-001', 'Hassan-i Sabbah', 1050, 'Assassin', 90),
  ('BRK-001', 'Hercules', 2010, 'Berserker', 80);