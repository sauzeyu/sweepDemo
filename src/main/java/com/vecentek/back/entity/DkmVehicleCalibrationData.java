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
public class DkmVehicleCalibrationData extends BaseEntity {
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
     * 蓝牙灵敏度等级
     */
    private String level;

    /**
     * 个人化信息和标定数据,用于界面展示
     */
    private String vehicleAndCalibrationString;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 删除标识，1删除 0正常
     */
    private String deleteFlag;

}
