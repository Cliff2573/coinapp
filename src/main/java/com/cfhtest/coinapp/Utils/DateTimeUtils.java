package com.cfhtest.coinapp.Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {
    
    public static Date stringToDate(String dateString) {

        // 1. 定義字串格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        // 2. 解析字串為 LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);

        // 3. 轉換為 java.util.Date
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        return date;
    }

}
