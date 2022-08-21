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
    /**
     * 菜单列表
     */
    private List<String> menuList;

}
