package com.vecentek.back.dto;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-06-01 14:00
 */
@Data
public class MonthCountDTO {
    private static final String[] MYSQL_MONTHS = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private String month;
    private Integer count;

    public static List<Integer> countToList(List<MonthCountDTO> checkList) {
        ArrayList<Integer> result = new ArrayList<>();
        for (MonthCountDTO check : checkList) {
            result.add(check.getCount());
        }
        return result;
    }

    public static List<String> generateMonthList() {
        ArrayList<String> result = new ArrayList<>();
        Date date = new Date();
        for (int i = MYSQL_MONTHS.length - 1; i >= 0; i--) {
            DateTime offset = DateUtil.offset(date, DateField.MONTH, -i);
            String format = DateUtil.format(offset, "yyyy-MM");
            result.add(format);
        }
        return result;
    }


    public static List<MonthCountDTO> checkMonthCount(List<MonthCountDTO> checkList) {
        List<MonthCountDTO> monthCountList = new ArrayList<>();

        for (String mysqlMonth : MYSQL_MONTHS) {
            MonthCountDTO monthCount = new MonthCountDTO();
            monthCount.setCount(0);
            monthCount.setMonth(mysqlMonth);
            monthCountList.add(monthCount);
        }
        for (MonthCountDTO monthCount : monthCountList) {
            for (MonthCountDTO check : checkList) {
                if (Objects.equals(monthCount.getMonth(), check.getMonth())) {
                    monthCount.setCount(check.getCount());
                    break;
                }
            }
        }
        return monthCountList;
    }

    public static List<MonthCountDTO> checkMonthCount(List<MonthCountDTO> checkList, List<String> monthList) {

        List<MonthCountDTO> monthCountList = new ArrayList<>();

        for (String mysqlMonth : monthList) {
            MonthCountDTO monthCount = new MonthCountDTO();
            monthCount.setCount(0);
            monthCount.setMonth(mysqlMonth);
            monthCountList.add(monthCount);
        }
        for (MonthCountDTO monthCount : monthCountList) {
            for (MonthCountDTO check : checkList) {
                if (Objects.equals(monthCount.getMonth(), check.getMonth())) {
                    monthCount.setCount(check.getCount());
                    break;
                }
            }
        }
        return monthCountList;
    }
}
