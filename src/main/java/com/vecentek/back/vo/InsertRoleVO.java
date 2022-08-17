package com.vecentek.back.vo;

import com.vecentek.back.entity.DkmRole;
import lombok.Data;

import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-07-12 20:03
 */
@Data
public class InsertRoleVO  extends DkmRole {
    ///**
    // * 角色代码
    // */
    //private Integer code;
    ///**
    // * 角色名称
    // */
    //private String roleName;
    ///**
    // * 角色描述
    // */
    //private String intro;
    /**
     * 菜单列表
     */
    private List<String> menuList;

}
