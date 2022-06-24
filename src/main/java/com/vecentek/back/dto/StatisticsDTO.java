package com.vecentek.back.dto;

import lombok.Data;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-30 17:06
 */
@Data
public class StatisticsDTO {
    private Integer vehicleCount;
    private Integer keyCount;
    private Integer keyUseCount;
    private Integer keyErrorCount;
}
