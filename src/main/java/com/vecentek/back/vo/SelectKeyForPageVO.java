package com.vecentek.back.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SelectKeyForPageVO {
    /**
     * 分页页数
     */
    private Integer pageIndex;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 车架号
     */
    private String vin;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 钥匙类型 1 车主， 2 非车主， 3 车主和非车主
     */
    private Integer keyType;
    /**
     * 周期最小值
     */
    private Integer periodMin;
    /**
     * 周期最大值
     */
    private Integer periodMax;
    /**
     * 周期单位 minute分 hour小时 day天
     */
    private String periodUnit;
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
    private String valFromStartTime;
    /**
     * 生效结束时间
     */
    private String valFromEndTime;
    /**
     * 失效开始时间
     */
    private String valToStartTime;
    /**
     * 失效结束时间
     */
    private String valToEndTime;
    /**
     * 钥匙状态多选
     */
    private Integer[] dkState;
}
