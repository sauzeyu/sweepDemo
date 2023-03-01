package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 钥匙操作日志(DkmKeyLog)实体类
 *
 * @author EdgeYu
 * @since 2021-12-02 14:54:41
 */

@Data
public class DkmKeyLog implements Serializable {
    private static final long serialVersionUID = 485887257926622961L;
    /**
     * 钥匙日志id
     */
    private Long id;
    /**
     * VIN
     */
    private String vin;
    /**
     * 数字钥匙ID
     */
    private String keyId;
    /**
     * 手机型号
     */
    private String phoneModel;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 手机品牌
     */
    private String phoneBrand;
    /**
     * 成败标识 1 成功 0 失败
     */
    private Integer flag;
    /**
     * 成败标识 1 成功 0 失败
     */
    @TableField(exist = false)
    private String flagVO;
    /**
     * 车控执行状态码
     */
    private String statusCode;
    /**
     * 故障原因
     */
    private String errorReason;
    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;
    private String creator;
    private String updator;
    private String createTime;
    private String updateTime;
    /**
     * 车辆品牌
     */

    private String vehicleBrand;
    /**
     * 车辆型号
     */

    private String vehicleModel;
    /**
     * 车型
     */
    private String vehicleType;


    /**
     * 操作类型
     */
    @TableField(exist = false)
    private String operationType;
}
