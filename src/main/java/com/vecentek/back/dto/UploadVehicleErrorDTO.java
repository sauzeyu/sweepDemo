package com.vecentek.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-09 10:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadVehicleErrorDTO {
    /**
     * vin 号
     */
    private String vin;
    /**
     * 描述
     */
    private String description;


}
