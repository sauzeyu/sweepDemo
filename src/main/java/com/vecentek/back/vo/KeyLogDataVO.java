package com.vecentek.back.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class KeyLogDataVO {
    /**
     * 分页页数
     */
    private Integer pageIndex;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 车辆vin号
     */
    private String vin;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 车控执行状态码
     */
    private String statusCode;
    /**
     * 钥匙状态：1 已启用，3 冻结 ，4 过期， 5 吊销
     */
    private Integer dkState;
    /**
     * 是否车主钥匙；1是， 0否
     */
    private Integer status;
}
