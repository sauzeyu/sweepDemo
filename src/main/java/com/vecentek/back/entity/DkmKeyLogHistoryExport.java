package com.vecentek.back.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * (DkmKeyLogHistoryExport)表实体类
 *
 * @author makejava
 * @since 2022-06-23 13:44:32
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DkmKeyLogHistoryExport extends BaseEntity implements Serializable {

    /**
     * 导出状态(0：正在导出；1：导出完成；2：导出失败)
     */

    private Integer exportStatus;

    /**
     * 任务名称
     */
    private String missionName;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 导出状态(0：钥匙记录；1：钥匙信息)
     */

    private int type;

}

