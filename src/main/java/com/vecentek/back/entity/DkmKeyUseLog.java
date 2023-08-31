package com.vecentek.back.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-24 16:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkmKeyUseLog extends BaseEntity {
    /**
     * 日志id
     */
    private Long id;
    /**
     * 车辆vin码
     */
    private String vin;
    /**
     * 钥匙id
     */
    private String keyId;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;
    /**
     * 状态码
     */
    private String statusCode;

}
