package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.format.annotation.DateTimeFormat;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-16 14:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DkmAftermarketReplacement extends BaseEntity {
    /**
     * 主键id
     */
    private Long id;
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
     * 设备序列号前端显示值(16进制ASCII 转 utf8字符串)
     */
    @TableField(exist = false)
    private String oldBluetoothSnHEX;

    /**
     * 设备序列号前端显示值(16进制ASCII 转 utf8字符串)
     */
    @TableField(exist = false)
    private String newBluetoothSnHEX;
    /**
     * 换件时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date replacementTime;

    /**
     * 设置设备序列号，并转换为UTF-8字符串存入hwDeviceSnHEX字段
     */
    public void setOldBluetoothSn(String oldBluetoothSn) {
        this.oldBluetoothSn = oldBluetoothSn;
        String asciiString = null;
        asciiString = getString(oldBluetoothSn, asciiString);
        this.oldBluetoothSnHEX = asciiString;
    }
    /**
     * 设置设备序列号，并转换为UTF-8字符串存入hwDeviceSnHEX字段
     */
    public void setNewBluetoothSn(String newBluetoothSn) {
        this.newBluetoothSn = newBluetoothSn;
        String asciiString = null;
        asciiString = getString(newBluetoothSn, asciiString);
        this.newBluetoothSnHEX = asciiString;
    }

    private String getString(String bluetoothSn, String asciiString) {
        try {
            byte[] bytes = Hex.decodeHex(bluetoothSn);
            asciiString = new String(bytes, StandardCharsets.US_ASCII);
        } catch (DecoderException e) {
            System.out.println("Invalid hex string");
        }
        return asciiString;
    }

}
