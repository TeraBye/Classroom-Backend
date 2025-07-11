-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: chat_service
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chats`
--

DROP TABLE IF EXISTS `chats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chats` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `conversation_id` bigint NOT NULL,
  `content` varchar(255) NOT NULL,
  `time` datetime NOT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  `sender` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `conversation_id` (`conversation_id`),
  KEY `idx_chats_time` (`time`),
  CONSTRAINT `chats_ibfk_1` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chats`
--

LOCK TABLES `chats` WRITE;
/*!40000 ALTER TABLE `chats` DISABLE KEYS */;
INSERT INTO `chats` VALUES (1,1,'Hello, how are you?','2025-07-08 19:20:30',0,'student001'),(2,1,'Hello, how are you?','2025-07-08 19:20:30',0,'student001'),(3,1,'I\'m fine','2025-07-08 19:20:30',0,'student002'),(4,1,'hehe','2025-07-09 16:52:08',0,'student001'),(5,1,'yêu','2025-07-09 16:52:24',0,'student001'),(6,2,'chào bạn!','2025-07-09 16:52:34',0,'student001'),(7,1,'hi','2025-07-09 17:09:42',0,'student001'),(8,1,'hi','2025-07-09 17:18:58',0,'student001'),(9,1,'po','2025-07-09 17:19:03',0,'student001'),(10,2,'hi','2025-07-09 17:37:10',0,'student001'),(11,1,'hi','2025-07-09 17:41:37',0,'student001'),(12,1,'eow','2025-07-09 17:44:52',0,'student001'),(13,1,'chào','2025-07-09 17:44:57',0,'student001'),(14,1,'hi','2025-07-09 17:45:01',0,'student001'),(15,1,'11','2025-07-09 17:45:06',0,'student001'),(16,1,'chào','2025-07-09 17:47:39',0,'student001'),(17,1,'xem','2025-07-09 17:47:44',0,'student001'),(18,1,'hello','2025-07-09 17:49:29',0,'student001'),(19,1,'how are u today','2025-07-09 17:49:39',0,'student001'),(20,1,'12','2025-07-09 17:51:05',0,'student001'),(21,1,'ki','2025-07-09 17:51:10',0,'student001'),(22,1,'78','2025-07-09 17:51:17',0,'student001'),(23,1,'cay','2025-07-09 17:52:30',0,'student001'),(24,2,'bạn ơi','2025-07-09 17:54:35',0,'student001'),(25,1,'ok','2025-07-09 17:54:43',0,'student001'),(26,1,'123','2025-07-09 17:54:47',0,'student001'),(27,2,'có lẽ','2025-07-09 17:57:38',0,'student001'),(28,1,'3','2025-07-09 17:58:04',0,'student001'),(29,1,'11','2025-07-09 17:58:15',0,'student001'),(30,1,'a','2025-07-09 18:01:10',0,'student001'),(31,1,'y','2025-07-09 18:03:22',0,'student001'),(32,1,'12','2025-07-09 18:03:38',0,'student001'),(33,1,'a','2025-07-09 18:04:01',0,'student001'),(34,1,'1','2025-07-09 18:04:05',0,'student001'),(35,1,'a','2025-07-09 18:06:06',0,'student001'),(36,1,'a','2025-07-09 18:15:07',0,'student001'),(37,1,'aq1','2025-07-09 18:20:13',0,'student001'),(38,1,'ok','2025-07-09 18:20:20',0,'student001'),(39,1,'69','2025-07-09 18:20:24',0,'student001'),(40,1,'cay không?','2025-07-09 18:23:52',0,'student001'),(41,1,'cay','2025-07-09 18:24:03',0,'student001'),(42,1,'123','2025-07-09 18:31:07',0,'student001'),(43,1,'hay','2025-07-09 18:33:59',0,'student002'),(44,1,'uk','2025-07-09 18:34:11',0,'student001'),(45,1,'chào','2025-07-09 18:47:19',0,'student001'),(46,1,'chào','2025-07-09 18:47:30',0,'student002'),(47,1,'ht','2025-07-09 18:48:37',0,'student001'),(48,1,'Bạn khỏe không?','2025-07-09 18:49:12',0,'student001'),(49,1,'khônh','2025-07-09 18:50:23',0,'student002'),(50,1,'Bạn thì sao?','2025-07-09 18:53:51',0,'student001'),(51,1,'tre','2025-07-09 18:57:12',0,'student001'),(52,1,'qư','2025-07-09 19:01:41',0,'student001'),(53,1,'cá','2025-07-09 19:06:55',0,'student002'),(54,1,'meow','2025-07-09 19:13:40',0,'student001'),(55,1,'uoi','2025-07-09 19:13:48',0,'student002'),(56,1,'63','2025-07-09 19:14:18',0,'student002'),(57,1,'sao','2025-07-09 19:14:29',0,'student001'),(58,1,'hi','2025-07-09 19:14:43',0,'student002'),(59,1,'chào','2025-07-09 19:15:59',0,'student001'),(60,1,'hai','2025-07-09 19:16:14',0,'student002'),(61,1,'ca','2025-07-09 19:17:35',0,'student002'),(62,1,'12','2025-07-09 19:26:17',0,'student001'),(63,1,'có vẻ','2025-07-09 19:36:03',0,'student001'),(64,1,'là','2025-07-09 19:36:16',0,'student001'),(65,1,'sa','2025-07-09 19:36:31',0,'student001'),(66,1,'ta','2025-07-09 19:38:19',0,'student002'),(67,1,'rae','2025-07-09 19:38:35',0,'student001'),(68,1,'băm','2025-07-09 19:39:21',0,'student002'),(69,1,'ta','2025-07-09 19:39:46',0,'student001'),(70,1,'2525','2025-07-09 19:39:55',0,'student002'),(71,1,'câu','2025-07-09 19:41:19',0,'student001'),(72,1,'âq','2025-07-09 19:41:30',0,'student001'),(73,1,'sao','2025-07-09 19:41:38',0,'student001'),(74,1,'khi','2025-07-09 19:42:43',0,'student002'),(75,1,'khi','2025-07-09 19:42:54',0,'student002'),(76,1,'12','2025-07-09 19:44:12',0,'student002'),(77,1,'1','2025-07-09 19:44:26',0,'student002'),(78,1,'hai','2025-07-09 19:49:06',0,'student002'),(79,1,'ae','2025-07-09 19:49:38',0,'student001'),(80,1,'tada','2025-07-09 19:51:47',0,'student002'),(81,1,'câu','2025-07-09 19:51:58',0,'student002'),(82,1,'tada','2025-07-09 19:52:33',0,'student002'),(83,1,'uk','2025-07-09 19:52:41',0,'student002'),(84,1,'ok','2025-07-09 19:52:47',0,'student002'),(85,1,'ok','2025-07-09 19:52:50',0,'student002'),(86,1,'sha','2025-07-09 19:53:00',0,'student002'),(87,1,'cúp','2025-07-09 19:54:45',0,'student002'),(88,1,'363','2025-07-09 19:54:52',0,'student002'),(89,1,'14','2025-07-09 19:54:55',0,'student002'),(90,1,'hi','2025-07-09 19:55:07',0,'student001'),(91,1,'chào','2025-07-09 19:55:15',0,'student001'),(92,1,'chào đằng ấy','2025-07-09 19:55:31',0,'student002'),(93,1,'umk','2025-07-09 19:55:39',0,'student001'),(94,1,'mồ','2025-07-09 19:55:46',0,'student001'),(95,1,'teri teri','2025-07-09 19:56:19',0,'student002'),(96,1,'haha','2025-07-09 19:56:23',0,'student001'),(97,1,'okok','2025-07-09 19:56:31',0,'student002'),(98,1,'không ổn lắm','2025-07-09 19:56:45',0,'student001'),(99,1,'tại vì','2025-07-09 19:56:52',0,'student001'),(100,1,'chưa đến lúc','2025-07-09 19:56:59',0,'student001'),(101,1,'chuẩn','2025-07-09 19:57:09',0,'student001'),(102,1,'kaka','2025-07-09 19:57:34',0,'student001');
/*!40000 ALTER TABLE `chats` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-10 20:23:43
