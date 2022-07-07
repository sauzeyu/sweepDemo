package com.vecentek.back.vo;

import com.vecentek.back.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *  导出钥匙信息分页查询条件
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkmKeyVO extends BaseEntity {



    /**
     * 钥匙id
     */
    private String id;
    /**
     * 车辆ID
     */
    private Integer vehicleId;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 车辆vin号
     */
    private String vin;

    /**
     * 周期开始时间
     */
    private Long cycleStartTime;

    /**
     * 周期结束时间
     */
    private Long cycleEndTime;

    /**
     * 周期单位
     */
    private String cycleUnit;

    /**
     * 申请开始时间
     */
    private String applyStartTime;

    /**
     * 申请结束时间
     */
    private String applyEndTime;

    /**
     * 生效开始时间
     */
    private String effectiveStartTime;

    /**
     * 生效结束时间
     */
    private String effectiveEndTime;

    /**
     * 失效开始时间
     */
    private String failStartTime;

    /**
     * 失效结束时间
     */
    private String failEndTime;

    /**
     * 钥匙类型:0 分享钥匙 1 车主钥匙
     */
    private Integer keyType;

    /**
     * 数字钥匙的具体状态(1：已启用 3：冻结 4：过期 5：吊销)
     */
    private Integer keyState;

}