package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DkmPhoneCalibrationData表实体类
 *
 * @author tangc
 * @version 1.0
 * @since 2022-04-01 15:57:49
 */

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DkmPhoneCalibrationData extends BaseEntity {
    private static final long serialVersionUID = 462498026447496254L;
    /**
     * 标定数据id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 车型
     */
    @Size(min = 1, max = 16, message = "车型长度最长为16位")
    @NotBlank(message = "车辆型号不能为空")
    private String vehicleModel;

    /**
     * 手机品牌
     */
    @Size(min = 1, max = 16, message = "手机品牌长度最长为16位")
    @NotBlank(message = "手机品牌不能为空")
    private String phoneBrand;

    /**
     * 手机型号
     */
    @Size(min = 1, max = 24, message = "手机型号长度最长为24位")
    @NotBlank(message = "手机型号不能为空")
    private String phoneModel;

    /**
     * 个人化信息和标定数据,用于界面展示
     */
    @NotBlank(message = "标定数据不能为空")
    @Size(min = 64, max = 64, message = "标定数据必须是32字节")
    private String personalAndCalibrationString;

    /**
     * 备注
     */
    private String remarks;
    /**
     * 车型
     */
    @Size(min = 1, max = 16, message = "车型长度最长为16位")
    private String vehicleType;
    /**
     * 车辆品牌
     */
    @Size(min = 1, max = 16, message = "车辆品牌长度最长为16位")
    private String vehicleBrand;

    /**
     * 特征点数据
     */
    private String featureData;


}
