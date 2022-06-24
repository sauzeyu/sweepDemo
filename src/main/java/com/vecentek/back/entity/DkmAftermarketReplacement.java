package com.vecentek.back.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
     * 换件时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date replacementTime;


}
