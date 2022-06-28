package com.vecentek.back.vo;

import lombok.Data;

@Data
/**
 * 时间段实体类
 */
public class TimeQuantumVO {
    /**
     * 起始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
}
