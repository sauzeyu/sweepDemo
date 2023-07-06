package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

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
     * 设备序列号前端显示值(16进制ASCII 转 utf8字符串)
     */
    @TableField(exist = false)
    private String hwDeviceSnHEX;
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

    /**
     * 设置设备序列号，并转换为UTF-8字符串存入hwDeviceSnHEX字段
     */
    public void setHwDeviceSn(String hwDeviceSn) {
        this.hwDeviceSn = hwDeviceSn;
        String asciiString = null;
        try {
            byte[] bytes = Hex.decodeHex(hwDeviceSn);
            asciiString = new String(bytes, StandardCharsets.US_ASCII);

        } catch (DecoderException e) {
            System.out.println("Invalid hex string");
        }
        this.hwDeviceSnHEX = asciiString;
    }

}
