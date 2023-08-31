package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-16 9:42
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DkmRole extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private String code;
    private String roleName;
    private String intro;

}
