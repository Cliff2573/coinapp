CREATE TABLE `currency` (
  `code` varchar(5) COMMENT '貨幣代碼',
  `symbol` varchar(5) DEFAULT NULL COMMENT '貨幣符號',
  `rate` varchar(20) DEFAULT NULL COMMENT '顯示的匯率（含千分位）',
  `description` varchar(255) DEFAULT NULL COMMENT '貨幣描述',
  `rate_float` decimal(10, 5) DEFAULT NULL COMMENT '浮點型匯率',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='貨幣資訊表';

CREATE TABLE `currency_l` (
  `code` varchar(5) COMMENT '貨幣代碼',
  `label` varchar(20) DEFAULT NULL COMMENT '貨幣中文名稱',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='貨幣中文名稱表';

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