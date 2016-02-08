-- MySQL dump 10.15  Distrib 10.0.17-MariaDB, for osx10.6 (i386)
--
-- Host: localhost    Database: sanc
-- ------------------------------------------------------
-- Server version	10.0.17-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tempcombinerequest`
--

DROP TABLE IF EXISTS `tempcombinerequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tempcombinerequest` (
  `id` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ka` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `kb` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `iva` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ivb` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ks` varchar(128) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='tempcombinerequest';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `temprenewrequest`
--

DROP TABLE IF EXISTS `temprenewrequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temprenewrequest` (
  `id` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ka` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `kb` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ks` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `iva` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ivb` varchar(128) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tempretireconfirm`
--

DROP TABLE IF EXISTS `tempretireconfirm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tempretireconfirm` (
  `id` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ka` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `iva` varchar(128) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tempretirerequest`
--

DROP TABLE IF EXISTS `tempretirerequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tempretirerequest` (
  `id` varchar(128) NOT NULL,
  `ka` varchar(128) NOT NULL,
  `kb` varchar(128) NOT NULL,
  `iva` varchar(128) NOT NULL,
  `ivb` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tempsignininterim`
--

DROP TABLE IF EXISTS `tempsignininterim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tempsignininterim` (
  `id` varchar(128) NOT NULL,
  `ka` varchar(128) NOT NULL,
  `iva` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tempsigninrequest`
--

DROP TABLE IF EXISTS `tempsigninrequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tempsigninrequest` (
  `id` varchar(2000) NOT NULL,
  `ka` varchar(128) NOT NULL,
  `kb` varchar(128) NOT NULL,
  `iva` varchar(128) NOT NULL,
  `ivb` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tempsignup`
--

DROP TABLE IF EXISTS `tempsignup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tempsignup` (
  `id` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ka` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `kb` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `kc` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ks` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `iva` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ivb` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ivc` varchar(128) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tempuncombinerequest`
--

DROP TABLE IF EXISTS `tempuncombinerequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tempuncombinerequest` (
  `id` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ka` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `iva` varchar(128) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userdevices`
--

DROP TABLE IF EXISTS `userdevices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userdevices` (
  `username` varchar(255) NOT NULL,
  `deviceKey` varchar(512) NOT NULL,
  `alias` varchar(512) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `username` varchar(255) NOT NULL,
  `password` varchar(512) NOT NULL,
  `passphrase` varchar(512) NOT NULL,
  `interimState` tinyint(1) NOT NULL DEFAULT '0',
  `tempCredentials` varchar(512) NOT NULL DEFAULT 'unknown',
  `throttling` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-23 17:15:13
