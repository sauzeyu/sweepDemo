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
 * @since 2022-03-18 16:07
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DkmMenu extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private String title;
    private String icon;
    private String href;
    private String target;
    private String isShow;
    private Integer type;
    private String dna;

}
