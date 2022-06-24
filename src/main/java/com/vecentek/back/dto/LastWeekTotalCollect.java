package com.vecentek.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-02-14 15:59
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastWeekTotalCollect {
    /**
     * 日期列表
     */
    private List<String> dateList;
    /**
     * 车辆统计列表
     */
    private List<Integer> vehicleTotalList;
    /**
     * 用户统计列表
     */
    private List<Integer> userTotalList;
    /**
     * 钥匙统计列表
     */
    private List<Integer> keyTotalList;

}
