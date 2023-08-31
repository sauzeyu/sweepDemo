package com.vecentek.back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-23 18:11
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VehicleBluetoothDTO {
    /**
     * 车辆vin码
     */
    private String vin;
    /**
     * 蓝牙序列号
     */
    private String hwDeviceSn;
    /**
     * 错误描述
     */
    private String errorDescription;


}
