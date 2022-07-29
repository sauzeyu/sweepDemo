package com.vecentek.back.vo;

import com.vecentek.back.entity.DkmRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-22 11:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO extends DkmRole {

    /**
     * 菜单列表
     */
    private List<String> menuList;
}

