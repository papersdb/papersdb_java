-- MySQL dump 10.13  Distrib 5.1.61, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: papersdb
-- ------------------------------------------------------
-- Server version	5.1.61-0ubuntu0.11.10.1

--
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
CREATE TABLE `author` (
  `id` bigint(20) NOT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `FAMILY_NAMES` varchar(255) DEFAULT NULL,
  `GIVEN_NAMES` varchar(255) DEFAULT NULL,
  `TITLE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EMAIL` (`EMAIL`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `author_paper`
--

DROP TABLE IF EXISTS `author_paper`;
CREATE TABLE `author_paper` (
  `AUTHOR_ID` bigint(20) NOT NULL,
  `PAPER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`PAPER_ID`,`AUTHOR_ID`),
  KEY `FKE640FDB8A32D0B24` (`AUTHOR_ID`),
  KEY `FKE640FDB8EB041BB0` (`PAPER_ID`),
  CONSTRAINT `FKE640FDB8EB041BB0` FOREIGN KEY (`PAPER_ID`) REFERENCES `paper` (`id`),
  CONSTRAINT `FKE640FDB8A32D0B24` FOREIGN KEY (`AUTHOR_ID`) REFERENCES `author` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `paper`
--

DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper` (
  `id` bigint(20) NOT NULL,
  `ABSTRACT` text,
  `CUSTOM_RANKING` varchar(255) DEFAULT NULL,
  `PUB_ENTRY_DATE` date DEFAULT NULL,
  `DOI` varchar(255) DEFAULT NULL,
  `KEYWORDS` varchar(255) DEFAULT NULL,
  `PUBLIC` bit(1) DEFAULT NULL,
  `RANKING_ID` varchar(255) DEFAULT NULL,
  `SUBMITTED_BY` varchar(255) DEFAULT NULL,
  `TITLE` varchar(255) DEFAULT NULL,
  `USER_TAGS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paper_attachment`
--

DROP TABLE IF EXISTS `paper_attachment`;
CREATE TABLE `paper_attachment` (
  `PAPER_ID` bigint(20) NOT NULL,
  `ATTACHMENT` varchar(255) DEFAULT NULL,
  KEY `FK8C91A5D6EB041BB0` (`PAPER_ID`),
  CONSTRAINT `FK8C91A5D6EB041BB0` FOREIGN KEY (`PAPER_ID`) REFERENCES `paper` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paper_collaboration`
--

DROP TABLE IF EXISTS `paper_collaboration`;
CREATE TABLE `paper_collaboration` (
  `PAPER_ID` bigint(20) NOT NULL,
  `COLLABORATION_ID` varchar(255) NOT NULL,
  PRIMARY KEY (`PAPER_ID`,`COLLABORATION_ID`),
  KEY `FK9F371092EB041BB0` (`PAPER_ID`),
  CONSTRAINT `FK9F371092EB041BB0` FOREIGN KEY (`PAPER_ID`) REFERENCES `paper` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `paper_paper`
--

DROP TABLE IF EXISTS `paper_paper`;
CREATE TABLE `paper_paper` (
  `FROM_PAPER_ID` bigint(20) NOT NULL,
  `TO_PAPER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`FROM_PAPER_ID`,`TO_PAPER_ID`),
  KEY `FK7B92D4D9F82834A5` (`FROM_PAPER_ID`),
  KEY `FK7B92D4D953E258F4` (`TO_PAPER_ID`),
  CONSTRAINT `FK7B92D4D953E258F4` FOREIGN KEY (`TO_PAPER_ID`) REFERENCES `paper` (`id`),
  CONSTRAINT `FK7B92D4D9F82834A5` FOREIGN KEY (`FROM_PAPER_ID`) REFERENCES `paper` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `paper_url`
--

DROP TABLE IF EXISTS `paper_url`;
CREATE TABLE `paper_url` (
  `PAPER_ID` bigint(20) NOT NULL,
  `URL` varchar(255) DEFAULT NULL,
  KEY `FKB7EFFBFCEB041BB0` (`PAPER_ID`),
  CONSTRAINT `FKB7EFFBFCEB041BB0` FOREIGN KEY (`PAPER_ID`) REFERENCES `paper` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `publication`
--

DROP TABLE IF EXISTS `publication`;
CREATE TABLE `publication` (
  `DISCRIMINATOR` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL,
  `DATE` datetime NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `BOOK_TITLE` varchar(255) DEFAULT NULL,
  `EDITION` varchar(255) DEFAULT NULL,
  `EDITOR` varchar(255) DEFAULT NULL,
  `CHAPTER` varchar(255) DEFAULT NULL,
  `NUMBER` varchar(255) DEFAULT NULL,
  `PAGES` varchar(255) DEFAULT NULL,
  `VOLUME` varchar(255) DEFAULT NULL,
  `INSTITUTION` varchar(255) DEFAULT NULL,
  `THESIS_TYPE` varchar(255) DEFAULT NULL,
  `BOOK_PUBLISHER` tinyblob,
  `PAPER_ID` bigint(20) DEFAULT NULL,
  `PUBLISHER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK400F360C61315AB0` (`PUBLISHER_ID`),
  KEY `FK400F360CEB041BB0` (`PAPER_ID`),
  CONSTRAINT `FK400F360CEB041BB0` FOREIGN KEY (`PAPER_ID`) REFERENCES `paper` (`id`),
  CONSTRAINT `FK400F360C61315AB0` FOREIGN KEY (`PUBLISHER_ID`) REFERENCES `publisher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `publisher`
--

DROP TABLE IF EXISTS `publisher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `publisher` (
  `id` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `RANKING_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `ACCESS_LEVEL` int(11) DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `FAMILY_NAMES` varchar(255) DEFAULT NULL,
  `GIVEN_NAMES` varchar(255) DEFAULT NULL,
  `LOGIN` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `IS_VERIFIED` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EMAIL` (`EMAIL`),
  UNIQUE KEY `LOGIN` (`LOGIN`)
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

-- Dump completed on 2012-04-08 20:14:02
