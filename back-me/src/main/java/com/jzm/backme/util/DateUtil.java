package com.jzm.backme.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author: jzm
 * @date: 2024-04-17 09:11
 **/

public class DateUtil extends cn.hutool.core.date.DateUtil
{


    public static final long S = 1000L;
    public static final long M = S * 60;
    public static final long H = M * 60;
    public static final long D = H * 24;

    public static LocalDateTime parseLDTs(String s_timestamp){
        // 是否为16进制数字
        if(s_timestamp.startsWith("0x")){
            s_timestamp = s_timestamp.substring("0x".length());
        }
        return parseLDTms(String.valueOf(Long.parseLong(s_timestamp) * 1000));
    }

    public static LocalDateTime parseLDT(String s_timestamp){
        return parseLDTs(s_timestamp);
    }

    public static LocalDateTime parseLDTms(String ms_timestamp){
        if(ms_timestamp.startsWith("0x")){
            ms_timestamp = ms_timestamp.substring("0x".length());
        }
        Date date = new Date(Long.parseLong(ms_timestamp));
        String dateTime = formatDateTime(date);
        return DateUtil.parseLocalDateTime(dateTime);
    }

    public static int compare(LocalDateTime d1,LocalDateTime d2){
        Date date1 = localToDate(d1);
        Date date2 = localToDate(d2);
        return DateUtil.compare(date1,date2);
    }


    public static LocalDateTime addTime(LocalDateTime source,int day){
        Long end = datetimeToTimestamp(source);
        end = end + day * D;
        return parseLDTms(String.valueOf(end));
    }

    public static Date localToDate(LocalDateTime localDateTime){
        ZonedDateTime l1 = localDateTime.atZone(ZoneId.systemDefault());
       return Date.from(l1.toInstant());
    }


    public static Long datetimeToTimestamp(LocalDateTime beginEndTime)
    {
        return beginEndTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}
