package com.ls.l7mall_2.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author laijs
 * @date 2020-3-29-12:35
 */
public class DateTimeUtil {
    // 将符合指定格式的时间字符串转化为Date类日期
    public static Date strToDate(String dateTimeString, String dateFormatStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatStr);
        Date date = simpleDateFormat.parse(dateTimeString);
        return date;
    }

    // 将Date类的日期按照指定格式生成字符串
    public static String dateToStr(Date date,String dateFormatStr){
        if(date == null){
            return StringUtils.EMPTY;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatStr);
        String format = simpleDateFormat.format(date);
        return format;
    }

    // 以上是使用SimpleDateFormat类完成时间的String和Date之间的转化

    // 借助joda-time也可以完成，另外
    // 在同一个项目中，格式String dateFormatStr可以提取为常量

    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date strToDate(String dateTimeString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeString);
        Date date = dateTime.toDate();
        return date;
    }

    public static String dateToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        String s = dateTime.toString();
        return s;
    }
}
