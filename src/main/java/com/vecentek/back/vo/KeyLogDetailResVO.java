package com.vecentek.back.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class KeyLogDetailResVO {
    /**
     * 车辆vin号
     */
    private String vin;
    /**
     * 车辆vin号
     */
    private String keyId;
    /**
     * 车控执行状态码
     */
    private String statusCode;
    /**
     * 车控执行状态名称
     */
    private String statusName;
    /**
     * 成败标识 1 成功 0 失败
     */
    private Integer flag;
    /**
     * 故障原因
     */
    private String errorReasonName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

}
