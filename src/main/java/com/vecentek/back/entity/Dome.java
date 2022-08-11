package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName test
 */
@TableName(value ="dome")
@Data
public class Dome implements Serializable {
    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 操作者
     */
    @TableField(value = "create_id")
    private Long createId;

    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 修改者
     */
    @TableField(value = "update_id")
    private Long updateId;

    /**
     * 更新时间
     */
    @TableField(value = "update_date")
    private Date updateDate;

    /**
     * 删除状态 0正常 1删除
     */
    @TableField(value = "delete_state")
    private Integer deleteState;

    /**
     *
     */
    @TableField(value = "title")
    private String title;

    /**
     *
     */
    @TableField(value = "cont")
    private String cont;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}
