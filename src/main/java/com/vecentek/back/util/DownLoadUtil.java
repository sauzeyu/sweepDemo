package com.vecentek.back.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.vecentek.back.constant.JwtConstant;
import com.vecentek.back.dto.LastWeekTotalDTO;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author liujz
 * @date 2022/7/6
 * @apiNote
 */
public class DownLoadUtil {


    /**
     *
     * @param startTime
     * @param endTime
     * @param token
     * @return 文件名，用户名
     */
    public static List<String> checkLastWeekTotal(String startTime, String endTime,String token) {
        List<String> dateList = new ArrayList<>();
        if (CharSequenceUtil.isBlank(startTime) && CharSequenceUtil.isBlank(endTime)){
            String now = DateUtil.now();
            DateTime dateTime = new DateTime(now, DatePattern.NORM_DATETIME_FORMAT);
            int month = dateTime.getMonth() + 1;
            int nextMonth;
            if(month == 12){
                nextMonth = 1;
            } else {
                nextMonth = month + 1 ;
            }

            startTime = getFirstDayOfMonth(month);
            endTime = getFirstDayOfMonth(nextMonth);

        }
        dateList.add(startTime);
        dateList.add(endTime);
        // 1Excel 文件名 文件格式 文件路径的提前处理
        // 1.1时间格式化格式


        // 1.2导出的excel按月份以时间命名 如2022-6-1~2022-7-1钥匙使用记录
        DateTime startName = DateUtil.parse(startTime);
        String startFileName = DateUtil.format(startName, "yyyy-MM-dd");

        DateTime endName = DateUtil.parse(endTime);
        String endFileName = DateUtil.format(endName, "yyyy-MM-dd");
        String fileName = startFileName + "~" + endFileName;

        String username = "";
        //if (CharSequenceUtil.isNotBlank(token)) {
        //    try {
        //        username  = TokenUtils.parseToken(JwtConstant.JWT_USER_INFO_KEY, token, String.class);
        //    } catch (NoSuchAlgorithmException e) {
        //        e.printStackTrace();
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    } catch (InvalidKeySpecException e) {
        //        e.printStackTrace();
        //    }
        //}


        dateList.add(fileName);
        dateList.add(username);

        return dateList;
    }




    /**
     * 获取当前月第一天
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

        return sdf.format(calendar.getTime())+" 00:00:00";
    }
}
