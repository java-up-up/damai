USE damai_user_1;

DROP TABLE IF EXISTS `d_ticket_user_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d_ticket_user_0` (
                                   `id` bigint NOT NULL COMMENT '主键id',
                                   `user_id` bigint NOT NULL COMMENT '用户id',
                                   `rel_name` varchar(256) NOT NULL COMMENT '用户真实名字',
                                   `id_type` int NOT NULL DEFAULT '1' COMMENT '证件类型 1:身份证 2:港澳台居民居住证 3:港澳居民来往内地通行证 4:台湾居民来往内地通行证 5:护照 6:外国人永久居住证',
                                   `id_number` varchar(512) NOT NULL COMMENT '证件号码',
                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                   `edit_time` datetime NOT NULL COMMENT '编辑时间',
                                   `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='购票人表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_ticket_user_0`
--

LOCK TABLES `d_ticket_user_0` WRITE;
/*!40000 ALTER TABLE `d_ticket_user_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_ticket_user_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_ticket_user_1`
--

DROP TABLE IF EXISTS `d_ticket_user_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d_ticket_user_1` (
                                   `id` bigint NOT NULL COMMENT '主键id',
                                   `user_id` bigint NOT NULL COMMENT '用户id',
                                   `rel_name` varchar(256) NOT NULL COMMENT '用户真实名字',
                                   `id_type` int NOT NULL DEFAULT '1' COMMENT '证件类型 1:身份证 2:港澳台居民居住证 3:港澳居民来往内地通行证 4:台湾居民来往内地通行证 5:护照 6:外国人永久居住证',
                                   `id_number` varchar(512) NOT NULL COMMENT '证件号码',
                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                   `edit_time` datetime NOT NULL COMMENT '编辑时间',
                                   `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='购票人表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_ticket_user_1`
--

LOCK TABLES `d_ticket_user_1` WRITE;
/*!40000 ALTER TABLE `d_ticket_user_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_ticket_user_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_user_0`
--

DROP TABLE IF EXISTS `d_user_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d_user_0` (
                            `id` bigint NOT NULL COMMENT '主键id',
                            `name` varchar(256) DEFAULT NULL COMMENT '用户名字',
                            `rel_name` varchar(256) DEFAULT NULL COMMENT '用户真实名字',
                            `mobile` varchar(512) NOT NULL COMMENT '手机号',
                            `gender` int NOT NULL DEFAULT '1' COMMENT '1:男 2:女',
                            `password` varchar(512) DEFAULT NULL COMMENT '密码',
                            `email_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否邮箱认证 1:已验证 0:未验证',
                            `email` varchar(256) DEFAULT NULL COMMENT '邮箱地址',
                            `rel_authentication_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否实名认证 1:已验证 0:未验证',
                            `id_number` varchar(512) DEFAULT NULL COMMENT '身份证号码',
                            `address` varchar(256) DEFAULT NULL COMMENT '收货地址',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
                            `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_user_0`
--

LOCK TABLES `d_user_0` WRITE;
/*!40000 ALTER TABLE `d_user_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_user_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_user_1`
--

DROP TABLE IF EXISTS `d_user_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d_user_1` (
                            `id` bigint NOT NULL COMMENT '主键id',
                            `name` varchar(256) DEFAULT NULL COMMENT '用户名字',
                            `rel_name` varchar(256) DEFAULT NULL COMMENT '用户真实名字',
                            `mobile` varchar(512) NOT NULL COMMENT '手机号',
                            `gender` int NOT NULL DEFAULT '1' COMMENT '1:男 2:女',
                            `password` varchar(512) DEFAULT NULL COMMENT '密码',
                            `email_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否邮箱认证 1:已验证 0:未验证',
                            `email` varchar(256) DEFAULT NULL COMMENT '邮箱地址',
                            `rel_authentication_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否实名认证 1:已验证 0:未验证',
                            `id_number` varchar(512) DEFAULT NULL COMMENT '身份证号码',
                            `address` varchar(256) DEFAULT NULL COMMENT '收货地址',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
                            `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_user_1`
--

LOCK TABLES `d_user_1` WRITE;
/*!40000 ALTER TABLE `d_user_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_user_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_user_email_0`
--

DROP TABLE IF EXISTS `d_user_email_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d_user_email_0` (
                                  `id` bigint NOT NULL COMMENT '主键id',
                                  `user_id` bigint NOT NULL COMMENT '用户id',
                                  `email` varchar(512) NOT NULL COMMENT '邮箱',
                                  `create_time` datetime NOT NULL COMMENT '创建时间',
                                  `edit_time` datetime NOT NULL COMMENT '编辑时间',
                                  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
                                  PRIMARY KEY (`id`),
                                  KEY `email_idx` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='用户邮箱表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_user_email_0`
--

LOCK TABLES `d_user_email_0` WRITE;
/*!40000 ALTER TABLE `d_user_email_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_user_email_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_user_email_1`
--

DROP TABLE IF EXISTS `d_user_email_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d_user_email_1` (
                                  `id` bigint NOT NULL COMMENT '主键id',
                                  `user_id` bigint NOT NULL COMMENT '用户id',
                                  `email` varchar(512) NOT NULL COMMENT '邮箱',
                                  `create_time` datetime NOT NULL COMMENT '创建时间',
                                  `edit_time` datetime NOT NULL COMMENT '编辑时间',
                                  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
                                  PRIMARY KEY (`id`),
                                  KEY `email_idx` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='用户邮箱表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_user_email_1`
--

LOCK TABLES `d_user_email_1` WRITE;
/*!40000 ALTER TABLE `d_user_email_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_user_email_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_user_mobile_0`
--

DROP TABLE IF EXISTS `d_user_mobile_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d_user_mobile_0` (
                                   `id` bigint NOT NULL COMMENT '主键id',
                                   `user_id` bigint NOT NULL COMMENT '用户id',
                                   `mobile` varchar(512) NOT NULL COMMENT '手机号',
                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                   `edit_time` datetime NOT NULL COMMENT '编辑时间',
                                   `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
                                   PRIMARY KEY (`id`),
                                   KEY `mobile_idx` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='用户手机表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_user_mobile_0`
--

LOCK TABLES `d_user_mobile_0` WRITE;
/*!40000 ALTER TABLE `d_user_mobile_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_user_mobile_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_user_mobile_1`
--

DROP TABLE IF EXISTS `d_user_mobile_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d_user_mobile_1` (
                                   `id` bigint NOT NULL COMMENT '主键id',
                                   `user_id` bigint NOT NULL COMMENT '用户id',
                                   `mobile` varchar(512) NOT NULL COMMENT '手机号',
                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                   `edit_time` datetime NOT NULL COMMENT '编辑时间',
                                   `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
                                   PRIMARY KEY (`id`),
                                   KEY `mobile_idx` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='用户手机表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_user_mobile_1`
--

LOCK TABLES `d_user_mobile_1` WRITE;
/*!40000 ALTER TABLE `d_user_mobile_1` DISABLE KEYS */;
INSERT INTO `d_user_mobile_1` VALUES (1818770834261614595,1818770834261614594,'280a7288b94ca19418b4ed5b710617a8','2026-01-22 15:43:35','2026-01-22 15:43:35',1);
/*!40000 ALTER TABLE `d_user_mobile_1` ENABLE KEYS */;
UNLOCK TABLES;
