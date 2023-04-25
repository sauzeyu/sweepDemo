package com.vecentek.back.exception;

import lombok.Data;

/**
 * @author ：liujz
 * @version ：1.0
 * @since 2022-04-24 18:15
 */
@Data
public class DiagnosticLogsException extends Exception {
    private String businessId;
    private String faultId;
    private Integer code;


    public DiagnosticLogsException(String businessId, String faultId) {
        this.businessId = businessId;
        this.faultId = faultId;
    }

    public DiagnosticLogsException(String businessId, String faultId, Integer code) {
        this.businessId = businessId;
        this.faultId = faultId;
        this.code = code;
    }
}
