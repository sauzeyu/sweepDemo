package com.vecentek.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-19 14:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkmKeyLifecycle extends BaseEntity {

    /**
     * 钥匙生命周期id
     */
    private Long id;
    /**
     * 钥匙id
     */
    private String keyId;
    /**
     * 车架号
     */
    private String vin;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 钥匙类型
     */
    private Integer keyType;
    /**
     * 钥匙状态
     */
    private Integer keyStatus;
    /**
     * 钥匙来源
     */
    private Integer keySource;
}
