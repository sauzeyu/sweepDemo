package com.vecentek.back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-29 14:46
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadBluetoothsErrorDTO {
    private String description;
    private String hwDeviceSn;
    private String bleMacAddress;
}
