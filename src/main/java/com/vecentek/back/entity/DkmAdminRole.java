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
 * @since 2022-03-16 15:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkmAdminRole extends BaseEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer adminId;
    private Integer roleId;
}
