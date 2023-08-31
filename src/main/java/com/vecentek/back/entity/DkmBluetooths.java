package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.vecentek.back.exception.VecentException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

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
public class DkmBluetooths extends BaseEntity {
    private static final long serialVersionUID = 910587834779611304L;
    /**
     * 设备序列号
     */
    @TableId
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
     * 蓝牙设备状态,状态: 1 正常 0 报废
     */
    private Integer flag;
    private Integer deviceStatus;

    /**
     * 蓝牙模块公钥
     */
    private String pubKey;

    /**
     * 设置设备序列号，并转换为UTF-8字符串存入hwDeviceSnHEX字段
     * @return
     */
    public void setHwDeviceSn(String hwDeviceSn) throws VecentException {
        this.hwDeviceSn = hwDeviceSn;
        String asciiString = null;
        try {
            byte[] bytes = Hex.decodeHex(hwDeviceSn);
            asciiString = new String(bytes, StandardCharsets.US_ASCII);

        } catch (DecoderException e) {
//            throw  new VecentException("错误格式的TBOX号");
//            return PageResp.fail("错误格式的TBOX号");
        }
        this.hwDeviceSnHEX = asciiString;

    }

}