INSERT INTO `currency` (`code`, `symbol`, `rate`, `description`, `rate_float`) VALUES
('USD', '$', '23,342.0112', 'US Dollar', 23342.0112),
('GBP', '£', '19,504.3978', 'British Pound Sterling', 19504.3978),
('EUR', '€', '22,738.5269', 'Euro', 22738.5269);

INSERT INTO `currency_l` (`code`, `label`) VALUES
('USD', '美金'),
('GBP', '英鎊'),
('EUR', '歐元');

INSERT INTO `currency_hist` (`code`, `rate_float`, `update_dttm`) VALUES
('USD', 23342.0112, '2022-08-01 00:00:00'),
('GBP', 19504.3978, '2022-08-01 00:00:00'),
('EUR', 22738.5269, '2022-08-01 00:00:00');