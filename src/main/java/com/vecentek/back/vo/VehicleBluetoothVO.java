package com.vecentek.back.vo;

import lombok.Data;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-23 11:45
 */
@Data
public class VehicleBluetoothVO {
    /**
     * 车辆vin号
     */
    private String vin;
    /**
     * 车辆车型
     */
    private String vehicleModel;
    /**
     * 车辆品牌
     */
    private String vehicleBrand;
    /**
     * 蓝牙模块序列号
     */
    private String hwDeviceSn;
    /**
     * 蓝牙模块检索号
     */
    private String searchNumber;
    /**
     * 蓝牙MAC地址
     */
    private String bleMacAddress;
    /**
     * 蓝牙公钥
     */
    private String pubKey;
    /**
     * 芯片id
     */
    private String dkSecUnitId;
    /**
     * 供应商编号
     */
    private String hwDeviceProviderNo;
}
