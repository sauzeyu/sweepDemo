package com.vecentek.back.dto;

import com.vecentek.back.entity.DkmKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 钥匙信息(DkmKey)实体类
 *
 * @author EdgeYu
 * @version ：1.0
 * @since 2021-11-30 17:30:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkmKeyDTO extends DkmKey {
    /**
     * 用户手机号
     */
    private String phone;
}
