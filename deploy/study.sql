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
	`NAME` varchar(255) DEFAULT NULL,
	`CREATE_TIME` datetime DEFAULT NULL,
	`MODIFY_TIME` datetime DEFAULT NULL,
	`LABEL` varchar(255) DEFAULT NULL,
	`CONTENT` text,
	`CLICKS` int(11) DEFAULT 0,
	`AGREES` int(11) DEFAULT 0,	
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
  `REPEAT` int(5) NOT NULL DEFAULT 1,
  `LAST_RECITE` datetime DEFAULT NULL,
  PRIMARY KEY (`USERNAME`,`WORD_NAME`),
  FOREIGN KEY (`WORD_NAME`) REFERENCES word(`WORD_NAME`) ON DELETE CASCADE,
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
INSERT INTO `user` (USERNAME,PASSWORD) VALUES ('123','202cb962ac59075b964b07152d234b70');
INSERT INTO `user` (USERNAME,PASSWORD) VALUES ('user01','202cb962ac59075b964b07152d234b70');
INSERT INTO `user` (USERNAME,PASSWORD) VALUES ('user02','202cb962ac59075b964b07152d234b70');
INSERT INTO `user` (USERNAME,PASSWORD) VALUES ('user03','202cb962ac59075b964b07152d234b70');

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

-- ----------------------------
-- Records of article
-- ----------------------------
insert into `article` (USERNAME,CATEGORY_ID,NAME,CONTENT) VALUES('123',1,'name11','aaa');
insert into `article` (USERNAME,CATEGORY_ID,NAME,CONTENT) VALUES('123',1,'name12','bbb');
insert into `article` (USERNAME,CATEGORY_ID,NAME,CONTENT) VALUES('123',1,'name13','bbb');
insert into `article` (USERNAME,CATEGORY_ID,NAME,CONTENT) VALUES('123',1,'name14','ccc');
insert into `article` (USERNAME,CATEGORY_ID,NAME,CONTENT) VALUES('123',1,'name15','ff');
insert into `article` (USERNAME,CATEGORY_ID,NAME,CONTENT) VALUES('123',1,'name16','aa');
insert into `article` (USERNAME,CATEGORY_ID,NAME,CONTENT) VALUES('123',1,'name17','aaffa');
insert into `article` (USERNAME,CATEGORY_ID,NAME,CONTENT) VALUES('123',2,'name21','aaa');
insert into `article` (USERNAME,CATEGORY_ID,NAME,CONTENT) VALUES('123',2,'name22','aaa');
insert into `article` (USERNAME,CATEGORY_ID,NAME,CONTENT) VALUES('123',2,'name23','aaa');
insert into `article` (USERNAME,CATEGORY_ID,NAME,CONTENT) VALUES('123',2,'name24','aaa');

-- ----------------------------
-- Records of comment
-- ----------------------------
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('123',1,'comment1');
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('123',1,'comment2');
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('123',1,'comment3');
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('123',1,'comment4');
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('123',1,'comment5');
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('123',1,'comment6');
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('123',1,'comment7');
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('123',1,'comment8');
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('123',1,'comment9');
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('123',1,'comment10');
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('123',2,'comment112');
insert into `comment` (USERNAME,ARTICLE_ID,CONTENT) VALUES('user01',1,'comment132');

-- -----------------------------
-- Records of word
-- -----------------------------
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('ambulance', 'media/wordAudio/ambulance.mpeg', 'media/wordPicture/ambulance.jpg', 'n. 救护车', '\r\n\r\nWe got an ambulance and rushed her to hospital', ' 我们叫了一辆救护车，赶紧把她送到了医院。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('angel', 'media/wordAudio/angel.mpeg', 'media/wordPicture/angel.jpg', 'n. 安琪儿;天使，天使般的人;守护神;善良可爱的人', 'Thank you a thousand times, you\'re an angel.', ' 万分感谢，你真是个大好人。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('appointment', 'media/wordAudio/appointment.mpeg', 'media/wordPicture/appointment.jpg', 'n. 任命;约会;职务;职位', 'His appointment to the Cabinet would please the right-wing.', ' 他被任命为内阁成员会让右翼党派很高兴。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('atmosphere', 'media/wordAudio/atmosphere.mpeg', 'media/wordPicture/atmosphere.jpg', 'n. 气氛;大气层;大气，空气;风格，基调', 'These gases pollute the atmosphere of towns and cities.', '这些气体污染城镇和城市的空气。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('audience', 'media/wordAudio/audience.mpeg', 'media/wordPicture/audience.jpg', 'n. 观众;听众;读者;接见', 'The entire audience broke into loud applause', '全场观众爆发出热烈的掌声。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('basin', 'media/wordAudio/basin.mpeg', 'media/wordPicture/basin.jpg', 'n. 盆地;流域;盆;水坑，池塘', 'Place the eggs and sugar in a large basin. ', ' 把鸡蛋和糖放在一个大碗里。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('blaze', 'media/wordAudio/blaze.mpeg', 'media/wordPicture/blaze.jpg', 'n. 火焰;光辉;爆发;光彩', 'Two firemen were hurt in a blaze which swept through a tower block last night.', ' 昨晚，一场大火吞噬了一座高层建筑，两位消防员被烧伤.');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('block', 'media/wordAudio/block.mpeg', 'media/wordPicture/block.jpg', 'n. 块;街区;<英>大楼，大厦;障碍物，阻碍', '\r\n\r\nShe walked four blocks down High Street ', '她沿着大街走了四条街。');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('bough', 'media/wordAudio/bough.mpeg', 'media/wordPicture/bough.jpg', '\r\nn. 大树枝', 'I rested my fishing rod against a pine bough. ', '我把钓竿搁在松树树枝上。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('campus', 'media/wordAudio/campus.mpeg', 'media/wordPicture/campus.jpg', 'n. 校园;校区', '\r\n\r\nPrivate automobiles are not allowed on campus. ', '私家车不准在校园里使用。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('canoe', 'media/wordAudio/canoe.mpeg', 'media/wordPicture/canoe.jpg', 'n. 独木舟，轻舟', 'We saw the canoe overturn, throwing its passengers into the water. ', '我们看到独木舟翻倒，把乘客扔进水中。');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('cherry', 'media/wordAudio/cherry.mpeg', 'media/wordPicture/cherry.jpg', 'n. 樱桃;樱桃树;樱桃色;处女膜', 'Light filtered into my kitchen through the soft, green shade of the cherry tree.', '光线透过樱桃树柔软、绿色的树荫照进我的厨房。');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('compass', 'media/wordAudio/compass.mpeg', 'media/wordPicture/compass.jpg', 'n. 罗盘;指南针;圆规;界限', 'We had to rely on a compass and a lot of luck to get here.', '我们必须依靠指南针和很多运气才能来到这里。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('concrete', 'media/wordAudio/concrete.mpeg', 'media/wordPicture/concrete.jpg', 'n. 混凝土;具体物;（图案式）有形诗;〔建〕钢筋混凝土', 'The posts have to be set in concrete', ' 这些柱子必须用混凝土固定。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('construction', 'media/wordAudio/construction.mpeg', 'media/wordPicture/construction.jpg', 'n. 建造;建筑物;解释;建造物', '\r\n\r\nHe\'d already started construction on a hunting lodge.', ' 他已经开始建造一间狩猎用的小屋。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('corridor', 'media/wordAudio/corridor.mpeg', 'media/wordPicture/corridor.jpg', 'n. 走廊，通道;走廊（一国领土通过他国境内的狭长地带）;[建]覆道', ' He wheeled the trolley down the corridor and disappeared with it into the service lift. ', ' 他推着小推车走过走廊，之后进了员工专用电梯不见了。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('crow', 'media/wordAudio/crow.mpeg', 'media/wordPicture/crow.jpg', 'n. 乌鸦;雄鸡的啼声', 'The cock crows and the dawn chorus begins.', ' 雄鸡报晓，鸟儿欢叫。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('cupboard', 'media/wordAudio/cupboard.mpeg', 'media/wordPicture/cupboard.jpg', 'n. 橱柜;壁橱;衣柜;食物柜', 'The kitchen cupboard was stocked with tins of soup and food.', ' 厨房的橱柜里备有汤罐头和食品罐头。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('dawn', 'media/wordAudio/dawn.mpeg', 'media/wordPicture/dawn.jpg', 'n. 黎明，拂晓;开端;醒悟', ' Throughout Europe a new railway age, that of the high-speed train, has dawned ', ' 新的铁路时代——高速铁路时代，在整个欧洲已经开始出现。 ');
INSERT INTO `word` (WORD_NAME,VOICE_ADDRESS,PICTURE_ADDRESS,WORD_TRANSLATE,WORD_EXAMPLE,EXAMPLE_TRANSLATE) VALUES ('defend', 'media/wordAudio/defend.mpeg', 'media/wordPicture/defend.jpg', 'vt.& vi. 保卫;辩护;（足球、曲棍球等）防守;进行辩护', 'Every man who could fight was now committed to defend the ridge', ' 每个能够参加战斗的男子现在都决心要保卫这条山脊。 ');


