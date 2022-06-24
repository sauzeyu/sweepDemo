package com.vecentek.back.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-01-18 17:50
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeVO {

    /**
     * 老用户id
     */
    private String oldUserId;
    /**
     * 新用户id
     */
    private String newUserId;
    /**
     * 新车主用户名
     */
    private String newUserName;
    /**
     * 新用户身份证
     */
    private String newUserIdCard;
    /**
     * 车辆id
     */
    private String vehicleId;
    /**
     * 变更时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String buyVehicleTime;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 车辆vin号
     */
    private String vin;
}
