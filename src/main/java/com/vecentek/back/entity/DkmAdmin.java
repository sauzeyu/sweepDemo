package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-11 10:44
 */

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DkmAdmin extends BaseEntity implements Serializable {

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户描述信息
     */
    private String extraInfo;
}