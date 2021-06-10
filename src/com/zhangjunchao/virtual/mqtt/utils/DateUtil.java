package com.zhangjunchao.virtual.mqtt.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String DATE_FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";


    public static String getCurrentTimeStr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_SECOND);
        return dateFormat.format(new Date());
    }

}
