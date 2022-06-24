package com.vecentek.back.vo;

import com.vecentek.back.entity.DkmAdmin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-16 13:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminVO extends DkmAdmin {
    private List<String> roleList;
}
