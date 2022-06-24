package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 手机用户信息(DkmUser)实体类
 *
 * @author EdgeYu
 * @version ：1.0
 * @since 2021-11-30 17:26:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkmUser extends BaseEntity {

    /**
     * 手机用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 姓名
     */
    private String username;

}
