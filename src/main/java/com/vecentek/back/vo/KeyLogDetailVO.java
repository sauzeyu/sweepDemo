package com.vecentek.back.vo;

import lombok.Data;

@Data
public class KeyLogDetailVO {
    /**
     * 车辆vin号
     */
    private String vin;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 车控执行状态码
     */
    private String statusCode;
}
