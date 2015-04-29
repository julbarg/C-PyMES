CREATE TABLE `alarm_pymes` (
  `id` bigint(15) unsigned NOT NULL AUTO_INCREMENT,
  `facility` varchar(10) DEFAULT NULL,
  `priority` varchar(10) DEFAULT NULL,
  `level` varchar(10) DEFAULT NULL,
  `program` varchar(15) DEFAULT NULL,
  `code_service` varchar(32) DEFAULT NULL,
  `ip` varchar(32) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `oid` varchar(60) DEFAULT NULL,
  `event_name` varchar(60) DEFAULT NULL,
  `message` text,
  `date` date DEFAULT NULL,
  `time` time DEFAULT NULL,
  `ip_user_acknowledge` varchar(17) DEFAULT NULL,
  `user_acknowledge` varchar(50) DEFAULT NULL,
  `datetime_acknowledge` datetime DEFAULT NULL,
  `estado` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ip` (`ip`),
  KEY `id` (`id`),
  KEY `name` (`name`),
  KEY `oid` (`oid`),
  KEY `date` (`date`),
  KEY `priority` (`priority`),
  KEY `facility` (`facility`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;