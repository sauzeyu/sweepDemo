package com.vecentek.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 蓝牙数据(DkmBluetooths)实体类
 *
 * @author EdgeYu
 * @version ：1.0
 * @since 2022-01-10 14:48:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkmBluetooths extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 910587834779611304L;
    /**
     * 设备序列号
     */
    private String hwDeviceSn;

    /**
     * 蓝牙模块检索号
     */
    private String searchNumber;
    /**
     * 设备供应商编号
     */
    private String hwDeviceProviderNo;
    /**
     * 数字钥匙软件版本号
     */
    private String dkSdkVersion;
    /**
     * 数字钥匙安全单元ID(如有安全芯片此处指安全芯片SEID)
     */
    private String dkSecUnitId;
    /**
     * 蓝牙MAC地址
     */
    private String bleMacAddress;
    /**
     * 二级密钥
     */
    private String masterKey;

    private String digKey;
    /**
     * 蓝牙设备状态，1：正常，2：报废
     */
    private Integer flag;

    /**
     * 蓝牙模块公钥
     */
    private String pubKey;


}
