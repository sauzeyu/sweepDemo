package com.vecentek.back.dto;

import com.vecentek.back.entity.DkmRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-21 13:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuDTO extends DkmRole {
    private List<String> menuIds;
}
