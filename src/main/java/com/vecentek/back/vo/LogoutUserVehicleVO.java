package com.vecentek.back.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-06-10 10:41
 */
@Data
public class LogoutUserVehicleVO {
    private String userId;
    private String vin;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logoutTime;
}
