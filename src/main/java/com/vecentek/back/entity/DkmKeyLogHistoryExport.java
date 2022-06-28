package com.vecentek.back.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@ApiModel(value = "dkm_key_log_history_export", description = "钥匙操作日志导出记录")
public class DkmKeyLogHistoryExport extends Model<DkmKeyLogHistoryExport> {

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
    private Date operateTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 更新人
     */
    private String updator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}

