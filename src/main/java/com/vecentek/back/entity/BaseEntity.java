package com.vecentek.back.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity 基本类，所有 Entity 的父类
 *
 * @author EdgeYu
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {
    /**
     * 编辑者
     */
    private String updator;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 编辑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
