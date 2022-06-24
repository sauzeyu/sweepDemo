package com.vecentek.back.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-01-14 15:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVehicleVO {
    /**
     * 用户名称
     */
    private String username;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 车牌号
     */
    private String license;
    /**
     * 车辆类型（个人用车，企业用车）
     */
    private String vehicleType;
    /**
     * 车辆vin号
     */
    private String vin;
    /**
     * 购车时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bindTime;

}
