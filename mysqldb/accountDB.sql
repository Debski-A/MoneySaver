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

DROP TABLE IF EXISTS `income_categories`;
CREATE TABLE `income_categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `incomes`;
CREATE TABLE `incomes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `frequency` varchar(10) NOT NULL,
  `currency` varchar(3) NOT NULL,
  `amount` decimal(11,2) NOT NULL,
  `date_of_income` date NOT NULL,
  `note` varchar(250),
  `uuid` binary(16) NOT NULL UNIQUE,
  `id_category` int(11) NOT NULL,
  `id_account` int(11) NOT NULL,
  KEY `fk_incomes_category_idx` (`id_category`),
  KEY `fk_accounts_idx` (`id_account`),
  CONSTRAINT `fk_incomes_category` FOREIGN KEY (`id_category`) REFERENCES `income_categories` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_accounts_incomes` FOREIGN KEY (`id_account`) REFERENCES `accounts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `outcome_categories`;
CREATE TABLE `outcome_categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `outcomes`;
CREATE TABLE `outcomes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `frequency` varchar(10) NOT NULL,
  `currency` varchar(3) NOT NULL,
  `amount` decimal(11,2) NOT NULL,
  `date_of_outcome` date NOT NULL,
  `note` varchar(250),
  `uuid` binary(16) NOT NULL UNIQUE,
  `id_category` int(11) NOT NULL,
  `id_account` int(11) NOT NULL,
  KEY `fk_outcomes_category_idx` (`id_category`),
  KEY `fk_accounts_idx` (`id_account`),
  CONSTRAINT `fk_outcomes_category` FOREIGN KEY (`id_category`) REFERENCES `outcome_categories` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_accounts_outcomes` FOREIGN KEY (`id_account`) REFERENCES `accounts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*DML*/
INSERT INTO `roles` VALUES (1,'ROLE_USER'),(2,'ROLE_PREMIUM');
INSERT INTO `income_categories` (name) VALUES ('payment'),('gift'),('benefit'),('other');
INSERT INTO `outcome_categories` (name) VALUES ('fee'),('food'),('alcohol'),('gift'),('car'),('other');
