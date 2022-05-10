-- use `mysql`
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
`userName` varchar(20) NOT NULL,
`chrName` varchar(20) default NULL,
`password` varchar(20) default NULL,
`role` varchar(20) default NULL,
PRIMARY KEY (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `user` VALUES ('admin', '管理员', '12345', '管理员');
INSERT INTO `user` VALUES ('niegang', '聂刚',  '12345', '收银员');
select * from user