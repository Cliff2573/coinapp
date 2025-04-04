CREATE TABLE `currency` (
  `sn` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水號',
  `code` varchar(5) DEFAULT NULL COMMENT '貨幣代碼',
  `symbol` varchar(5) DEFAULT NULL COMMENT '貨幣符號',
  `rate` varchar(20) DEFAULT NULL COMMENT '顯示的匯率（含千分位）',
  `description` varchar(255) DEFAULT NULL COMMENT '貨幣描述',
  `rate_float` decimal(10, 5) DEFAULT NULL COMMENT '浮點型匯率',
  PRIMARY KEY (`sn`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='貨幣資訊表';

CREATE TABLE `currency_label` (
  `code` varchar(5) DEFAULT NULL COMMENT '貨幣代碼',
  `label` varchar(20) DEFAULT NULL COMMENT '貨幣中文名稱'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='貨幣代碼中文表';