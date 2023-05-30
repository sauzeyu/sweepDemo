package com.vecentek.back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author edgeyu
 * @version 1.0
 * @since 2023/4/23 15:09
 */
@EqualsAndHashCode()
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DkmFunctionalAbnormal {

    /**
     *
     */
    private long id;

    /**
     * 来源id
     */
    private String sourceId;

    private String source;

    /**
     * 业务id
     */
    private String businessId;
    /**
     * 业务
     */
    private String business;

    /**
     * 功能异常id
     */
    private String faultId;

    /**
     * 功能异常
     */
    private String fault;

    /**
     * 解决方案
     */
    private List<String> solution;
}
