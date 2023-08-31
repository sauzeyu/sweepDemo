package com.vecentek.back.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-19 16:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkmUserVehicle extends BaseEntity {
    /**
     * 用户车辆表id
     */
    private Long id;
    /**
     * 车辆id
     */
    private Integer vehicleId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 绑定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bindTime;
    /**
     * 解绑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date unbindTime;
    /**
     * 绑定状态
     */
    private Integer bindStatus;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 设备相关信息
     */
    private String deviceInfo;
    /**
     * 车端信息
     */
    private String carInfo;
    /**
     * 车架号
     */
    private String vin;
    private String ks;
    /**
     * 车辆类型（个人用车，企业用车）
     */
    private String vehicleType;
    /**
     * 车牌号
     */
    private String license;


}
