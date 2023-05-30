package com.vecentek.back.exception;

import lombok.Builder;
import lombok.Data;

/**
 * @author ：liujz
 * @version ：1.0
 * @since 2022-04-24 18:15
 */
@Data
@Builder
public class DiagnosticLogsException extends Exception {
    private String businessId;
    private String faultId;
    private Integer code;
    private String vin;
    private String userId;


    public DiagnosticLogsException(String businessId, String faultId) {
        this.businessId = businessId;
        this.faultId = faultId;
    }

    public DiagnosticLogsException(String businessId, String faultId, Integer code) {
        this.businessId = businessId;
        this.faultId = faultId;
        this.code = code;
    }


    public DiagnosticLogsException(String businessId, String faultId, Integer code,String vin) {
        this.businessId = businessId;
        this.faultId = faultId;
        this.code = code;
        this.vin = vin;
    }
    public DiagnosticLogsException(String businessId, String faultId, Integer code,String vin, String userId) {
        this.businessId = businessId;
        this.faultId = faultId;
        this.code = code;
        this.vin = vin;
        this.userId = userId;
    }
}
