CREATE DATABASE  IF NOT EXISTS `accountDB`;
USE `accountDB`;

DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL UNIQUE,
  `password` varchar(80) NOT NULL,
  `enabled` tinyint(4) NOT NULL,
  `email` varchar(60) NOT NULL,
  `uuid` binary(16) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `accounts_roles`;
CREATE TABLE `accounts_roles` (
  `id_account` int(11) NOT NULL,
  `id_role` int(11) NOT NULL,
  PRIMARY KEY (`id_account`,`id_role`),
  KEY `fk_roles_account_idx` (`id_role`),
  CONSTRAINT `fk_accounts_roles` FOREIGN KEY (`id_account`) REFERENCES `accounts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_roles_account` FOREIGN KEY (`id_role`) REFERENCES `roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `roles` VALUES (1,'ROLE_USER'),(2,'ROLE_PREMIUM');
