CREATE DATABASE  IF NOT EXISTS `accountDB`;
USE `accountDB`;

DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL UNIQUE,
  `password` varchar(80) NOT NULL,
  `enabled` tinyint(4) NOT NULL,
  `email` varchar(60) NOT NULL,
  `role` smallint NOT NULL,
  `uuid` binary(16) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `incomes`;
CREATE TABLE `incomes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` decimal(11,2) NOT NULL,
  `currency` smallint NOT NULL,
  `frequency` smallint NOT NULL,
  `date_of_income` date NOT NULL,
  `note` varchar(250),
  `income_category` smallint NOT NULL,
  `uuid` binary(16) NOT NULL UNIQUE,
  `id_account` int(11) NOT NULL,
  KEY `fk_accounts_idx` (`id_account`),
  CONSTRAINT `fk_accounts_incomes` FOREIGN KEY (`id_account`) REFERENCES `accounts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `outcomes`;
CREATE TABLE `outcomes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` decimal(11,2) NOT NULL,
  `currency` smallint NOT NULL,
  `frequency` smallint NOT NULL,
  `date_of_outcome` date NOT NULL,
  `note` varchar(250),
  `outcome_category` smallint NOT NULL,
  `uuid` binary(16) NOT NULL UNIQUE,
  `id_account` int(11) NOT NULL,
  KEY `fk_accounts_idx` (`id_account`),
  CONSTRAINT `fk_accounts_outcomes` FOREIGN KEY (`id_account`) REFERENCES `accounts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
