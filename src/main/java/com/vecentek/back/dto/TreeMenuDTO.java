package com.vecentek.back.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-18 15:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TreeMenuDTO {
    @JsonIgnore
    private Integer parentId;

    private String title;
    private String key;
    private List<TreeMenuDTO> children;

}
