package com.vecentek.back.util;

import com.vecentek.back.dto.LastWeekTotalDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-02-14 14:02
 */
public class EchartsUtil {

    public static final Integer TOTAL_DAYS = 7;

    private EchartsUtil() {

    }


    /**
     * 检查最近七日数据，如当日无新增数据，置0
     *
     * @param lastWeekTotal 最近七日新增数据
     * @return 最近七日新增数列表
     */
    public static List<Integer> checkLastWeekTotal(List<LastWeekTotalDTO> lastWeekTotal) {
        Map<String, Integer> map = new TreeMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date();
        List<Integer> totalList = new ArrayList<>();

        startDate.setDate(startDate.getDate() - TOTAL_DAYS);

        for (int i = 0; i < TOTAL_DAYS; i++) {
            startDate.setDate(startDate.getDate() + 1);
            String format = simpleDateFormat.format(startDate);
            map.put(format, 0);
        }

        lastWeekTotal.forEach(total -> map.put(total.getDays(), total.getTotal()));

        map.forEach((day, total) -> totalList.add(total));


        return totalList;
    }

    public static List<String> lastWeekDayList() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date();
        List<String> dateList = new ArrayList<>();
        startDate.setDate(startDate.getDate() - TOTAL_DAYS);


        for (int i = 0; i < TOTAL_DAYS; i++) {
            startDate.setDate(startDate.getDate() + 1);
            String format = simpleDateFormat.format(startDate);
            dateList.add(format);
        }
        return dateList;
    }


}
