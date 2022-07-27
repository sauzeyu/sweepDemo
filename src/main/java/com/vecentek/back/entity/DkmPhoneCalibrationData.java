package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    private String vehicleModel;

    /**
     * 手机品牌
     */
    private String phoneBrand;

    /**
     * 手机型号
     */
    private String phoneModel;

    /**
     * 个人化信息和标定数据,用于界面展示
     */
    private String personalAndCalibrationString;

    /**
     * 备注
     */
    private String remarks;
    /**
     * 车型
     */
    private String vehicleType;
    /**
     * 车辆品牌
     */
    private String vehicleBrand;

}
