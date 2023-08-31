package com.vecentek.back.dto;

import lombok.Data;

import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-06-01 14:21
 */
@Data
public class UseLogAndErrorLogTotalByMonthDTO {
    private String name;
    private List<Integer> useLogCount;
}
