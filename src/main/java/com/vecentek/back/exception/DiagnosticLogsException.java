package com.vecentek.back.exception;

import lombok.Data;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-01-17 18:15
 */
@Data
public class DiagnosticLogsException extends Exception {
    private String businessId;
    private String faultId;


    public DiagnosticLogsException(String businessId, String faultId) {
        this.businessId = businessId;
        this.faultId = faultId;
    }
}
