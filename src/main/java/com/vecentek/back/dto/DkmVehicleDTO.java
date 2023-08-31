package com.vecentek.back.dto;

import com.vecentek.back.entity.DkmVehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class DkmVehicleDTO extends DkmVehicle {
    /**
     * 车主id
     */
    private String userId;
    /**
     * 用车类型
     */
    private String vehicleType;
    /**
     * 车牌号
     */
    private String license;
}
