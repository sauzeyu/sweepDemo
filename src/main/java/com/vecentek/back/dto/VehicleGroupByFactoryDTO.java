package com.vecentek.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页车辆工厂分布图 DTO
 *
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-02-11 11:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleGroupByFactoryDTO {
    private Integer value;
    private String name;
}
