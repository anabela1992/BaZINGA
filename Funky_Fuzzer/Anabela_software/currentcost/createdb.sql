CREATE DATABASE energy;
USE energy;

CREATE USER 'yyyy'@'localhost' IDENTIFIED BY 'xxxx';

GRANT SELECT , INSERT , UPDATE , DELETE, LOCK TABLES ON * . * TO 'yyyy'@'localhost' IDENTIFIED BY 'xxxx';

CREATE TABLE IF NOT EXISTS `consumption` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `power` smallint(3) NOT NULL DEFAULT '100',
  `temp` tinyint(2) NOT NULL,
  `joules` tinyint(2) NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;

INSERT INTO consumption (power, temp, joules, time) VALUES
(1, 0, 0, now()),
(2, 0, 0, now()),
(3, 0, 0, now()),
(4, 0, 0, now()),
(5, 0, 0, now()),
(6, 0, 0, now()),
(7, 0, 0, now()),
(8, 0, 0, now()),
(9, 0, 0, now()),
(10, 0, 0, now());
