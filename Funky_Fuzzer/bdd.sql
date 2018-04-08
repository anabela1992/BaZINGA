USE dvwa;

GRANT ALL PRIVILEGES ON dvwa . * TO 'root'@'localhost' IDENTIFIED BY 'root' WITH GRANT OPTION;

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` tinyint(4) NOT NULL auto_increment,
  `first_name` varchar(255) collate latin1_general_ci default NULL,
  `last_name` varchar(127) collate latin1_general_ci default NULL,
  PRIMARY KEY  (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=1 ;

-- 
-- Dumping data for table `books`
--
INSERT INTO `users` VALUES (1, 'John', 'Smith');
INSERT INTO `users` VALUES (2, 'Mary', 'Jane');
INSERT INTO `users` VALUES (3, 'Tom', 'Sawyer');
INSERT INTO `users` VALUES (4, 'Jack', 'Koontz');
