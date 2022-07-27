package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 车辆(DkmVehicle)实体类
 *
 * @author EdgeYu
 * @version ：1.0
 * @since 2021-11-30 17:26:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkmVehicle extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 829654883855131675L;

    /**
     * 车辆ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 所属工厂编号
     */
    private String factoryNo;
    /**
     * VIN
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
     * 车型
     */
    private String vehicleType;

    /**
     * 蓝牙编号
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
     * 蓝牙MAC地址
     */
    private String bleMacAddress;
    /**
     * 蓝牙模块公钥
     */
    private String pubKey;
    /**
     * 数字钥匙安全单元ID(如有安全芯片此处指安全芯片SEID)
     */
    private String dkSecUnitId;

}
