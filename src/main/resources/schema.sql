CREATE TABLE `currency` (
  `code` varchar(5) COMMENT '貨幣代碼',
  `symbol` varchar(5) DEFAULT NULL COMMENT '貨幣符號',
  `rate` varchar(20) DEFAULT NULL COMMENT '匯率（含千分位，字串）',
  `description` varchar(255) DEFAULT NULL COMMENT '貨幣描述',
  `rate_float` decimal(10, 5) DEFAULT NULL COMMENT '匯率（數值格式）',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='貨幣資訊表';

CREATE TABLE `currency_l` (
  `code` varchar(5) COMMENT '貨幣代碼',
  `label` varchar(20) DEFAULT NULL COMMENT '貨幣中文名稱',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='貨幣中文名稱表';

CREATE TABLE `currency_hist` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(5) DEFAULT NULL COMMENT '貨幣代碼',
  `rate_float` decimal(10, 5) DEFAULT NULL COMMENT '匯率',
  `update_dttm` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='貨幣匯率變更紀錄表';

CREATE OR REPLACE VIEW currency_view AS
SELECT 
    c.code,  -- 貨幣代碼
    c.symbol,  -- 貨幣符號
    c.rate,  -- 顯示的匯率（含千分位）
    c.description,  -- 貨幣說明
    c.rate_float,  -- 匯率（數值格式）
    cl.label  -- 貨幣中文名稱
FROM currency c
LEFT JOIN currency_l cl ON c.code = cl.code;