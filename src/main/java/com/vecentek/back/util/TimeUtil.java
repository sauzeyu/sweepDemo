package com.vecentek.back.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.vecentek.back.config.ProConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author liujz
 * @date 2022/7/6
 * @apiNote
 */
public class TimeUtil {
    /**
     * 默认日期格式
     */
    public static String DEFAULT_FORMAT = "yyyy-MM-dd 00:00:00";

    /**
     * Excel 文件名 文件格式 文件路径的提前处理
     *
     * @param startTime
     * @param endTime
     * @param token
     * @return 文件名，用户名
     */
    public static List<String> checkLastWeekTotal(String startTime, String endTime, String token) {
        List<String> dateList = new ArrayList<>();
        //对时间参数进行校验
        if (CharSequenceUtil.isBlank(startTime) && CharSequenceUtil.isBlank(endTime)) {
            //获取当前月和下一个月
            String now = cn.hutool.core.date.DateUtil.now();
            DateTime dateTime = new DateTime(now, DatePattern.NORM_DATETIME_FORMAT);
            int month = dateTime.getMonth() + 1;
            int nextMonth;
            if (month == 12) {
                nextMonth = 1;
            } else {
                nextMonth = month + 1;
            }
            //获取月份的第一天
            startTime = getFirstDayOfMonth(month);
            endTime = getFirstDayOfMonth(nextMonth);

        }
        if (CharSequenceUtil.isBlank(startTime)) {
            startTime = "";
        }
        if (CharSequenceUtil.isBlank(endTime)) {
            endTime = "";
        }
        dateList.add(startTime);
        dateList.add(endTime);


        //导出的excel按月份以时间命名 如2022-6-1~2022-7-1钥匙使用记录
        DateTime startName = cn.hutool.core.date.DateUtil.parse(startTime);
        String startFileName = cn.hutool.core.date.DateUtil.format(startName, "yyyy-MM-dd");

        DateTime endName = cn.hutool.core.date.DateUtil.parse(endTime);
        String endFileName = cn.hutool.core.date.DateUtil.format(endName, "yyyy-MM-dd");
        String fileName = startFileName + "~" + endFileName;

        String username = "";


        dateList.add(fileName);
        dateList.add(username);

        return dateList;
    }


    /**
     * 获取当前月第一天
     *
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(calendar.getTime()) + " 00:00:00";
    }

    /**
     * 格式化日期
     *
     * @param date 日期对象
     * @return String 日期字符串
     */
    public static String formatDate(Date date) {
        SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);
        String sDate = f.format(date);
        return sDate;
    }

    /**
     * 获取当年的第一天
     *
     * @param
     * @return
     */
    public static String getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取明年的第一天
     *
     * @param
     * @return
     */
    public static String getTommorwYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear + 1);
    }

    /**
     * 获取当年的最后一天
     *
     * @param
     * @return
     */
    public static String getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static String getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        String yearFirst = new SimpleDateFormat(DEFAULT_FORMAT).format(currYearFirst);
        return yearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static String getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        String yearLast = new SimpleDateFormat(DEFAULT_FORMAT).format(currYearLast);
        return yearLast;
    }

    public static String getNow() {


        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat(DEFAULT_FORMAT).format(cal.getTime());


    }

    public static String getNextDay() {


        Calendar cal = Calendar.getInstance();
        String today = new SimpleDateFormat(DEFAULT_FORMAT).format(cal.getTime());

        cal.add(Calendar.DATE, 1);//这里改为1
        Date time = cal.getTime();
        System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).format(time));
        String tommorw = new SimpleDateFormat(DEFAULT_FORMAT).format(time);
        return tommorw;
    }

    public static String getLastDay() {


        Calendar cal = Calendar.getInstance();
        String today = new SimpleDateFormat(DEFAULT_FORMAT).format(cal.getTime());

        cal.add(Calendar.DATE, -1);//这里改为1
        Date time = cal.getTime();
        System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).format(time));
        String tommorw = new SimpleDateFormat(DEFAULT_FORMAT).format(time);
        return tommorw;
    }

    /**
     * 获取下一个月的第一天
     */
    public static String getPerFirstDayOfMonth() {

        SimpleDateFormat dft = new SimpleDateFormat(DEFAULT_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());

    }


    /**
     * 获取去年的当月的最后一天
     *
     * @param
     * @return
     */
    public static String getMonthYearLast() {
        SimpleDateFormat dft = new SimpleDateFormat(DEFAULT_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return dft.format(calendar.getTime());
    }


    //获取系统初始时间，确定安时间分表后的最小表名
    public static String getSysDate() {
        ProConfig proConfig = SpringContextUtil.getBean(ProConfig.class);
        if (StrUtil.isEmpty(proConfig.getSysDate())) {
            throw new RuntimeException("未设置系统初始时间");
        }

        return proConfig.getSysDate() + " 00:00:00";
    }


}
