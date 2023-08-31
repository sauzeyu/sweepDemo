package com.vecentek.back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) // 字段list为空时不返回
public class ParentRouterDTO {
    private String path;
    private String title;
    private List<ChildRouterDTO> routes;

}
