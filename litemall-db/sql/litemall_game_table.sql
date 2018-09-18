use litemall;

--
DROP TABLE IF EXISTS `litemall_experience_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_experience_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户表的用户ID',
  `experience` int(11) NOT NULL DEFAULT '0' COMMENT '经验值',
  `action` varchar(200) NOT NULL DEFAULT '0' COMMENT '产生经验值的事件',
  `opera` tinyint(1) NOT NULL DEFAULT 0 COMMENT '经验值的增减符号',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁字段',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户经验值日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `litemall_game_gift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_game_gift` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) NOT NULL DEFAULT '0' COMMENT '礼品的名称',
  `type` varchar(65) NOT NULL DEFAULT '0' COMMENT '礼品的类型,(piece,product)',
  `rule_name` varchar(150) NOT NULL DEFAULT '0' COMMENT '礼品兑换规则',
  `rule_type` varchar(65) NOT NULL DEFAULT 'exchange' COMMENT '礼品兑换类型，（exchange，none,random-exchange）',
  `need_piece` int(11) NOT NULL DEFAULT 0 COMMENT '需要碎片数量',
  `need_fruit` int(11) NOT NULL DEFAULT 0 COMMENT '需要人参果数量',
  `random_str` varchar(65) NOT NULL DEFAULT 0 COMMENT '随机数范围',
  `before` int(11) NOT NULL DEFAULT 0 COMMENT '产生碎片的首次区间',
  `after` int(11) NOT NULL DEFAULT 0 COMMENT '产生碎片的下次区间',
  `comment` varchar(65) NOT NULL DEFAULT '' COMMENT '礼品的备注',
  `exchange_comment` varchar(200) NOT NULL DEFAULT '' COMMENT '礼品兑换方式',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏礼品表';
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `litemall_user_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_user_package` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户表的用户ID',
  `gift_id` int(11) NOT NULL DEFAULT '0' COMMENT '礼品ID',
  `status` varchar(65) NOT NULL DEFAULT '0' COMMENT '礼品的可使用状态',
  `game_id` int(11) NOT NULL DEFAULT '0' COMMENT '夺宝游戏的ID',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁字段',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `gift_id` (`gift_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户背包表';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `litemall_exchange_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_exchange_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户表的用户ID',
  `gift_id` int(11) NOT NULL DEFAULT '0' COMMENT '礼品ID',
  `gift_name` varchar(200) NOT NULL DEFAULT '' COMMENT '礼品名称',
  `need_piece` int(11) NOT NULL DEFAULT 0 COMMENT '需要碎片数量',
  `need_fruit` int(11) NOT NULL DEFAULT 0 COMMENT '需要人参果数量',
  `status` varchar(65) NOT NULL DEFAULT 'pending' COMMENT '兑换礼品的状态，pending：已兑换，success：到手',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁字段',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `gift_id` (`gift_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户兑换记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `litemall_game_recharge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_game_recharge` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recharge_id` varchar(65) NOT NULL COMMENT '游戏充值订单的ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户表的用户ID',
  `recharge_money` int(11) NOT NULL DEFAULT '0' COMMENT '充值金额',
  `xianyuan` int(11) NOT NULL DEFAULT 0 COMMENT '兑换的宝藏仙缘数量',
  `prepay_id` varchar(100) NOT NULL DEFAULT '0' COMMENT '预付单ID',
  `status` varchar(65) NOT NULL DEFAULT 'pending' COMMENT '充值订单的状态',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁字段',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `prepay_id` (`prepay_id`),
  KEY `recharge_id` (`recharge_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏充值订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `litemall_member_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_member_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member_order_id` varchar(65) NOT NULL COMMENT '会员订单的ID',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户表的用户ID',
  `member_money` int(11) NOT NULL DEFAULT '0' COMMENT '充值金额',
  `prepay_id` varchar(100) NOT NULL DEFAULT '0' COMMENT '预付单ID',
  `old_level` int(2) NOT NULL DEFAULT 0 COMMENT '之前的等级',
  `now_level` int(2) NOT NULL DEFAULT 0 COMMENT '现在的等级',
  `status` varchar(65) NOT NULL DEFAULT 'pending' COMMENT '充值订单的状态',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁字段',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `prepay_id` (`prepay_id`),
  KEY `member_order_id` (`member_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值会员订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `litemall_game_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_game_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户表的用户ID',
  `gift_id` int(11) NOT NULL DEFAULT '0' COMMENT '礼品表的礼品ID',
  `type` varchar(65) NOT NULL DEFAULT '0' COMMENT '游戏的类型，（give，generate）',
  `status` varchar(65) NOT NULL DEFAULT '0' COMMENT '游戏使用状态',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁字段',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值会员订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `litemall_member_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_member_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL COMMENT '会员名称',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '会员等级',
  `money` int(11) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员等级表';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `litemall_recharge_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_recharge_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `money` varchar(150) NOT NULL COMMENT '充值金额',
  `xianyuan` int(11) NOT NULL DEFAULT '0' COMMENT '兑换得到的宝藏仙缘',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值兑换规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `litemall_game_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_game_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `experience` int(11) NOT NULL DEFAULT '0' COMMENT '经验值',
  `xianyuan` int(11) NOT NULL DEFAULT '0' COMMENT '宝藏仙缘',
  `point` int(11) NOT NULL DEFAULT '0' COMMENT '积分',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁字段',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `litemall_point_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_point_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `point` int(11) NOT NULL DEFAULT '0' COMMENT '积分',
  `action` varchar(200) NOT NULL DEFAULT '0' COMMENT '产生经验值的事件',
  `opera` tinyint(1) NOT NULL DEFAULT 0 COMMENT '经验值的增减符号',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁字段',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户积分表';
/*!40101 SET character_set_client = @saved_cs_client */;


INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`,`need_fruit`,`before`,`after`, `comment`, `exchange_comment`) VALUES ('丰田卡罗拉1.8L E-CVT领先版', 'piece', '4888个碎片可兑换', 'exchange', '4888','200000','30','55','集齐碎片即可兑换价值15万', '集齐碎片后填写表格，由我方专人联系');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`,`need_fruit`,`before`,`after`, `comment`) VALUES ('99.9%纯金金条10克', 'piece', '999个碎片可兑换', 'exchange', '999','5000','30','10', '集齐碎片即可兑换价值4000元');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`comment`) VALUES ('99.9%铂金金条10克', 'piece', '999个碎片可兑换', 'exchange', '999', '5000','30','10','集齐碎片即可兑换价值4000元');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`comment`) VALUES ('50元话费', 'piece', '25个碎片可兑换', 'exchange', '25','100', '15','6','于碎片库点击“兑换”');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`exchange_comment`) VALUES ('666元现金红包', 'piece', '75个碎片可兑换', 'exchange', '75','700','20','10', '于碎片库点击“兑换”');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`exchange_comment`) VALUES ('288元现金红包', 'piece', '58个碎片可兑换', 'exchange', '58','500','20','7', '于碎片库点击“兑换”');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `before`,`after`,`exchange_comment`) VALUES ('成长人参果', 'fruit', '会员成长值随机3分——5分', 'none','5','4', '直接获得');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`exchange_comment`) VALUES ('格力1.5匹挂式空调', 'piece', '699个碎片可兑换', 'exchange', '699','3000','15','4', '直接获得');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`exchange_comment`) VALUES ('骨折霸王餐卷200元', 'piece', '20个碎片可兑换', 'exchange', '20', '150','10','20','先与我们联系');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`exchange_comment`) VALUES ('新马泰3国价值15000元', 'piece', '1399碎片可兑换', 'exchange', '1399','10000', '15','25','与我们联系。我们安排');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`random_str`,`exchange_comment`) VALUES ('随机红包2-20元', 'redPacket', '直接开奖', 'random-exchange', '0','50','10','25','2-20', '于碎片库点击“兑换”');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`comment`, `exchange_comment`) VALUES ('宝藏神秘盒', 'piece', '12个碎片可兑换', 'exchange', '12','3000','30','40', '随机20个-100个随机高级碎片', '直接使用');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`comment`, `exchange_comment`) VALUES ('九转易筋丹', 'piece', '8个碎片可兑换', 'exchange', '8','300','20','35', '一个月内最多使用3次', '1，非会员用户不可使用');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`comment`, `exchange_comment`) VALUES ('高级藏宝图', 'piece', '5个碎片即可兑换', 'exchange', '5','900','20','45', '随机1-5个高级碎片', '直接使用');
INSERT INTO `litemall`.`litemall_game_gift` (`name`, `type`, `rule_name`, `rule_type`, `need_piece`, `need_fruit`,`before`,`after`,`comment`, `exchange_comment`) VALUES ('欢乐1+0', 'piece', '3个碎片可兑换', 'exchange', '3', '100','15','10','增加10次抽奖次数', '每天最多可以用2次');

INSERT INTO `litemall`.`litemall_recharge_rule` (`money`, `xianyuan`) VALUES ('5', '500');
INSERT INTO `litemall`.`litemall_recharge_rule` (`money`, `xianyuan`) VALUES ('20', '2300');
INSERT INTO `litemall`.`litemall_recharge_rule` (`money`, `xianyuan`) VALUES ('50', '5800');
INSERT INTO `litemall`.`litemall_recharge_rule` (`money`, `xianyuan`) VALUES ('200', '22000');

INSERT INTO `litemall`.`litemall_member_level` (`name`, `level`, `money`) VALUES ('青铜会员', '0', '5');
INSERT INTO `litemall`.`litemall_member_level` (`name`, `level`, `money`) VALUES ('白银会员', '1', '10');
INSERT INTO `litemall`.`litemall_member_level` (`name`, `level`, `money`) VALUES ('黄金会员', '2', '15');
INSERT INTO `litemall`.`litemall_member_level` (`name`, `level`, `money`) VALUES ('钻石会员', '3', '20');

