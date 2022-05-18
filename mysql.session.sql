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


create table product(
    barcode varchar(20) not null,
    productName VARCHAR(20) not null,
    price decimal(10,2) not null,
    supplyer varchar(20) not null
)
insert into product 
values('001','test1',5.00,'test_supplyer');
insert into product 
values('002','test2',2.50,'test_supplyer');

select * from product

create table salesdetail(
    transaction_id VARCHAR(20) not null,
    barcode VARCHAR(20) not null,
    productName VARCHAR(20) not null,
    price decimal(10,2) not null,
    amount int not null,
    operator VARCHAR(20) not null,
    saleTime DATETIME not null,
    primary key(transaction_id),
    FOREIGN KEY (barcode) REFERENCES product(barcode)
)

