package com.vecentek.back.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vecentek.back.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-17 17:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftermarketReplacementDTO extends BaseEntity {
    /**
     * 车架号
     */
    private String vin;
    /**
     * 旧蓝牙设备序列号
     */
    private String oldBluetoothSn;
    /**
     * 新蓝牙设备序列号
     */
    private String newBluetoothSn;
    /**
     * 换件时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date replacementTime;
    /**
     * 蓝牙模块检索号
     */
    private String searchNumber;
    /**
     * 蓝牙MAC地址
     */
    private String bleMacAddress;

    /**
     * 蓝牙模块零件号
     */
    private String hwDevicePartNumber;

    /**
     * 蓝牙模块版本号
     */
    private String bleSoftwareVersion;

    /**
     * 蓝牙模块硬件版本号
     */
    private String bleHardwareVersion;
    /**
     * 蓝牙厂商
     */
    private String bleManufacturer;
    /**
     * 蓝牙模块蓝牙芯片软件号
     */
    private String dkSecUnitId;

    /**
     * 蓝牙模块公钥
     */
    private String pubKey;
    /**
     * 蓝牙模块供应商硬件号
     */
    private String hwDeviceHardwareNumber;
    /**
     * 蓝牙模块供应商编号
     */
    private String hwDeviceProviderNo;

}
