package com.vecentek.back.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DkDateUtils {

    //生成一个时间戳
    public long generateId() {
        return System.currentTimeMillis();
    }

    //生成一个不重复的ID
    public static String getUnionId() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String str = formatter.format(new Date());
        str +=  (int)((Math.random() * 9 + 1) * 1000000);
        String random=str.substring(4,20);
        return  random;
    }

}