/*
 Navicat Premium Data Transfer

 Source Server         : Localhost
 Source Server Type    : MySQL
 Source Server Version : 50621
 Source Host           : localhost
 Source Database       : icehouse

 Target Server Type    : MySQL
 Target Server Version : 50621
 File Encoding         : utf-8

 Date: 12/15/2016 19:55:24 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `account_id` int(11) NOT NULL AUTO_INCREMENT,
  `account_username` varchar(25) NOT NULL,
  `account_password` varchar(32) NOT NULL,
  `account_realname` varchar(50) NOT NULL,
  `account_address` text,
  `account_countrycode` varchar(2) NOT NULL DEFAULT '',
  `account_phonenumber` varchar(14) DEFAULT NULL,
  `account_balance` int(7) DEFAULT NULL,
  `account_status` tinyint(1) DEFAULT '2' COMMENT '1 = active, 2 = not active',
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `account`
-- ----------------------------
BEGIN;
INSERT INTO `account` VALUES ('1', 'wisnuwardoyo', '460e856c77238830a16d0cbfda5f189c', 'Wisnu Wardoyo', 'Jakarta', 'ID', '+6282299171396', '10000', '1');
COMMIT;

-- ----------------------------
--  Table structure for `machine`
-- ----------------------------
DROP TABLE IF EXISTS `machine`;
CREATE TABLE `machine` (
  `machineId` int(11) NOT NULL,
  `machineName` varchar(25) DEFAULT NULL,
  `machineStatus` tinyint(1) DEFAULT '2' COMMENT '1 = active, 2 = non-active',
  PRIMARY KEY (`machineId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `machine`
-- ----------------------------
BEGIN;
INSERT INTO `machine` VALUES ('22004', 'Highway Machine - Kemang', '1');
COMMIT;

-- ----------------------------
--  Table structure for `machine_tariff`
-- ----------------------------
DROP TABLE IF EXISTS `machine_tariff`;
CREATE TABLE `machine_tariff` (
  `machinetariff_id` int(11) NOT NULL,
  `machinetariff_machineid` int(11) DEFAULT NULL,
  `machinetariff_tariffid` int(11) DEFAULT NULL,
  PRIMARY KEY (`machinetariff_id`),
  UNIQUE KEY `machinetariff_machineid` (`machinetariff_machineid`) USING BTREE,
  KEY `machinetariff_tariffid` (`machinetariff_tariffid`),
  CONSTRAINT `machineId` FOREIGN KEY (`machinetariff_machineid`) REFERENCES `machine` (`machineId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tariffId` FOREIGN KEY (`machinetariff_tariffid`) REFERENCES `tariff` (`tariff_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `machine_tariff`
-- ----------------------------
BEGIN;
INSERT INTO `machine_tariff` VALUES ('1', '22004', '2');
COMMIT;

-- ----------------------------
--  Table structure for `tariff`
-- ----------------------------
DROP TABLE IF EXISTS `tariff`;
CREATE TABLE `tariff` (
  `tariff_id` int(11) NOT NULL,
  `tariff_name` varchar(25) DEFAULT NULL,
  `tariff_amount` int(7) DEFAULT NULL,
  PRIMARY KEY (`tariff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `tariff`
-- ----------------------------
BEGIN;
INSERT INTO `tariff` VALUES ('1', 'Toll Kemang - Gol I', '3000'), ('2', 'Toll Kemang - Gol II', '5000'), ('3', 'Toll Kemang - Gol III', '10000');
COMMIT;

-- ----------------------------
--  Table structure for `transaction`
-- ----------------------------
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
  `transcation_id` int(11) NOT NULL AUTO_INCREMENT,
  `transaction_routesid` int(11) DEFAULT NULL,
  `transaction_accountid` int(11) DEFAULT NULL,
  `transaction_timestamp` timestamp NULL DEFAULT NULL,
  `transaction_detail` text,
  `transcation_status` tinyint(2) DEFAULT NULL COMMENT '0 = Success, 1 = Failed, 2 = Insuffienct Balance',
  PRIMARY KEY (`transcation_id`),
  UNIQUE KEY `unique_trx` (`transaction_routesid`,`transaction_accountid`,`transaction_timestamp`),
  KEY `transaction_type` (`transaction_routesid`),
  CONSTRAINT `transaction_routes` FOREIGN KEY (`transaction_routesid`) REFERENCES `transaction_routes` (`transactionroutes_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `transaction_routes`
-- ----------------------------
DROP TABLE IF EXISTS `transaction_routes`;
CREATE TABLE `transaction_routes` (
  `transactionroutes_id` int(5) NOT NULL,
  `transactionroutes_routes` text,
  `transactionroutes_description` text,
  PRIMARY KEY (`transactionroutes_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `transaction_routes`
-- ----------------------------
BEGIN;
INSERT INTO `transaction_routes` VALUES ('1', 'http://localhost:9000/transaction/payment/v1/toll', 'Routes For Toll Payment'), ('2', 'http://localhost:9000/transaction/payment/v2/bevvend', 'Routes For Vending Machine Kubota Only');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
