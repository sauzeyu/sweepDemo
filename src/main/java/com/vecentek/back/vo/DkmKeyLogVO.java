package com.vecentek.back.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vecentek.back.entity.DkmKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 钥匙信息(DkmKey)实体类
 *
 * @author EdgeYu
 * @version ：1.0
 * @since 2021-11-30 17:30:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkmKeyLogVO extends DkmKey {
    /**
     * VIN
     */
    private String vin;
    /**
     * 数字钥匙ID
     */
    private String keyId;
    /**
     * 手机型号
     */
    private String phoneModel;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 手机品牌
     */
    private String phoneBrand;
    /**
     * 操作结果 1 成功 0 失败
     */
    private Integer flag;
    /**
     * 操作类型
     */
    private String statusCode;

    /**
     * 操作开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 操作结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
