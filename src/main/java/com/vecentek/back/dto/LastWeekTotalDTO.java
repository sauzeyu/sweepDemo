package com.vecentek.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计最近七天总数 DTO
 *
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-02-14 13:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastWeekTotalDTO {

    /**
     * 按天统计的日期
     */
    private String days;
    /**
     * 总数
     */
    private Integer total;

}
