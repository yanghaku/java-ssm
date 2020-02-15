DROP DATABASE IF EXISTS `study`;
CREATE DATABASE `study`;
use study;

-- 1. TABLE administrator
-- 密码存储对应的md5值

DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator`(
	`NAME` varchar(255) NOT NULL,
	`PASSWORD` char(32) DEFAULT NULL,
	`PHONE_NUMBER` varchar(11) DEFAULT NULL,
	PRIMARY KEY (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 2. TABLE user

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `USERNAME` varchar(255) NOT NULL,
  `PASSWORD` char(32) NOT NULL,
  `LEVEL` int(1) DEFAULT 0 NOT NULL,
  `NICKNAME` varchar(255) DEFAULT NULL,
  `BIRTHDAY` date DEFAULT NULL,
  `PHONE_NUMBER` varchar(11) DEFAULT NULL,
  `REGISTER_TIME` datetime DEFAULT NULL,
  `QQ_NUMBER` varchar(11) DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 3. TABLE article_category
DROP TABLE IF EXISTS `article_category`;
CREATE TABLE `article_category` (
	`CATEGORY_ID` int(11) NOT NULL AUTO_INCREMENT,
	`NAME` varchar(255) NOT NULL,
	PRIMARY KEY (`CATEGORY_ID`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 4. TABLE article

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
	`ARTICLE_ID` int(11) NOT NULL AUTO_INCREMENT,
	`USERNAME` varchar(255) NOT NULL,
	`CATEGORY_ID` int(11) NOT NULL,
	`TITLE` varchar(255) DEFAULT NULL,
	`CREATE_TIME` datetime DEFAULT NULL,
	`MODIFY_TIME` datetime DEFAULT NULL,
	`CONTENT` text,
	`CLICKS` int(11) DEFAULT 0,
	PRIMARY KEY (`ARTICLE_ID`),
	FOREIGN KEY (`CATEGORY_ID`) REFERENCES article_category(`CATEGORY_ID`) ON DELETE CASCADE,
	FOREIGN KEY (`USERNAME`) REFERENCES user(`USERNAME`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- 5. TABLE comment

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `COMMENT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ARTICLE_ID` int(11) NOT NULL,
  `USERNAME` varchar(255) NOT NULL,
  `CONTENT` text,
  `CREATE_TIME` datetime DEFAULT NULL,
  `REF_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`COMMENT_ID`),
  FOREIGN KEY (`ARTICLE_ID`) REFERENCES article(`ARTICLE_ID`) ON DELETE CASCADE,
  FOREIGN KEY (`USERNAME`) REFERENCES user(`USERNAME`) ON DELETE CASCADE,
  FOREIGN KEY (`REF_ID`) REFERENCES comment(`COMMENT_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- 6. TABLE word 

DROP TABLE IF EXISTS `word`;
CREATE TABLE `word` (
  `WORD_NAME` varchar(255) NOT NULL,
  `WORD_TYPE` int(1) NOT NULL DEFAULT 0,
  `VOICE_ADDRESS` varchar(255) DEFAULT NULL,
  `PICTURE_ADDRESS` varchar(255) DEFAULT NULL,
  `WORD_TRANSLATE` varchar(255) NOT NULL,
  `WORD_EXAMPLE` varchar(255) DEFAULT NULL,
  `EXAMPLE_TRANSLATE` varchar(255) DEFAULT NULL,
  `SITUATION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`WORD_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 7. TABLE video

DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `VIDEO_ID` int(11) NOT NULL AUTO_INCREMENT,
  `VIDEO_NAME` varchar(255) NOT NULL,
  `VIDEO_ADDRESS` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `LABEL` varchar(255) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`VIDEO_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- 8. TABLE article_collection

DROP TABLE IF EXISTS `article_collection`;
CREATE TABLE `article_collection` (
  `USERNAME` varchar(255) NOT NULL,
  `ARTICLE_ID` int(11) NOT NULL,
  `TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`USERNAME`,`ARTICLE_ID`),
  FOREIGN KEY (`ARTICLE_ID`) REFERENCES article(`ARTICLE_ID`) ON DELETE CASCADE,
  FOREIGN KEY (`USERNAME`) REFERENCES user(`USERNAME`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 9. TABLE word_collection

DROP TABLE IF EXISTS `word_collection`;
CREATE TABLE `word_collection` (
  `USERNAME` varchar(255) NOT NULL,
  `WORD_NAME` varchar(255) NOT NULL,
  `TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`USERNAME`,`WORD_NAME`),
  FOREIGN KEY (`WORD_NAME`) REFERENCES word(`WORD_NAME`) ON DELETE CASCADE,
  FOREIGN KEY (`USERNAME`) REFERENCES user(`USERNAME`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 10. TABLE video_collection

DROP TABLE IF EXISTS `video_collection`;
CREATE TABLE `video_collection` (
  `USERNAME` varchar(255) NOT NULL,
  `VIDEO_ID` int(11) NOT NULL,
  `TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`USERNAME`,`VIDEO_ID`),
  FOREIGN KEY (`VIDEO_ID`) REFERENCES video(`VIDEO_ID`) ON DELETE CASCADE,
  FOREIGN KEY (`USERNAME`) REFERENCES user(`USERNAME`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 11. TABLE word_recited

DROP TABLE IF EXISTS `word_recited`;
CREATE TABLE `word_recited` (
  `USERNAME` varchar(255) NOT NULL,
  `WORD_NAME` varchar(255) NOT NULL,
  `REPEATS` int(5) NOT NULL DEFAULT 1,
  `LAST_RECITE` datetime DEFAULT NULL,
  PRIMARY KEY (`USERNAME`,`WORD_NAME`),
  FOREIGN KEY (`WORD_NAME`) REFERENCES word(`WORD_NAME`) ON DELETE CASCADE,
  FOREIGN KEY (`USERNAME`) REFERENCES user(`USERNAME`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 12. TABLE article_agree
DROP TABLE IF EXISTS `article_agree`;
CREATE TABLE `article_agree` (
  `USERNAME` varchar(255) NOT NULL,
  `ARTICLE_ID` int(11) NOT NULL,
  `TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`USERNAME`,`ARTICLE_ID`),
  FOREIGN KEY (`ARTICLE_ID`) REFERENCES article(`ARTICLE_ID`) ON DELETE CASCADE,
  FOREIGN KEY (`USERNAME`) REFERENCES user(`USERNAME`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 13. TABLE article_keyword
DROP TABLE IF EXISTS `article_keyword`;
CREATE TABLE `article_keyword` (
  `ARTICLE_ID` int(11) NOT NULL,
  `KEYWORD` varchar(255) NOT NULL,
  `TFIDF` double,
  PRIMARY KEY (`ARTICLE_ID`,`KEYWORD`),
  FOREIGN KEY (`ARTICLE_ID`) REFERENCES article(`ARTICLE_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 14. TABLE user_keyword
DROP TABLE IF EXISTS `user_keyword`;
CREATE TABLE `user_keyword` (
  `USERNAME` varchar(255) NOT NULL,
  `KEYWORD` varchar(255) NOT NULL,
  `TFIDF` double,
  PRIMARY KEY (`USERNAME`,`KEYWORD`),
  FOREIGN KEY (`USERNAME`) REFERENCES user(`USERNAME`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 15. TABLR article_recommend
DROP TABLE IF EXISTS `article_recommend`;
CREATE TABLE `article_recommend` (
  `USERNAME` varchar(255) NOT NULL,
  `ARTICLE_ID` int(11) NOT NULL,
  `SIMILARITY` double,
  PRIMARY KEY (`USERNAME`,`ARTICLE_ID`),
  FOREIGN KEY (`ARTICLE_ID`) REFERENCES article(`ARTICLE_ID`) ON DELETE CASCADE,
  FOREIGN KEY (`USERNAME`) REFERENCES user(`USERNAME`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;





-- ----------------------------
-- Records of admmin
-- md5('admin') = '21232f297a57a5a743894a0e4a801fc3'
-- ----------------------------
INSERT INTO `administrator` VALUES ('admin','21232f297a57a5a743894a0e4a801fc3',1961882079);

-- ----------------------------
-- Records of user
-- md5('123') = '202cb962ac59075b964b07152d234b70'
-- ----------------------------
insert into user values('123','202cb962ac59075b964b07152d234b70',3,'nick1','20200214','13465043362','20200214001200','10086','111@111.com');
insert into user values('user02','202cb962ac59075b964b07152d234b70',3,'nick2','20200214','13465043362','20200214001200','10010','222@111.com');
insert into user values('user03','202cb962ac59075b964b07152d234b70',3,'nick3','20200214','13465043362','20200214001200','10086','333@111.com');
insert into user values('user04','202cb962ac59075b964b07152d234b70',3,'nick4','20200214','13465043362','20200214001200','10086','444@111.com');


-- ----------------------------
-- Records of article_category
-- ----------------------------
INSERT INTO `article_category` VALUES(null,'英语新闻');
INSERT INTO `article_category` VALUES(null,'英语散文');
INSERT INTO `article_category` VALUES(null,'英语故事');
INSERT INTO `article_category` VALUES(null,'英语笑话');
INSERT INTO `article_category` VALUES(null,'英语科普');
INSERT INTO `article_category` VALUES(null,'英语娱乐');
INSERT INTO `article_category` VALUES(null,'英语诗歌');
INSERT INTO `article_category` VALUES(null,'英语演讲');
INSERT INTO `article_category` VALUES(null,'英语试题');
INSERT INTO `article_category` VALUES(null,'英语行业');
INSERT INTO `article_category` VALUES(null,'英语小说');
INSERT INTO `article_category` VALUES(null,'英语技巧');
INSERT INTO `article_category` VALUES(null,'英语论坛');
