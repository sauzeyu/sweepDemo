package com.vecentek.back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChildRouterDTO {
    private String path;
    private String title;
    private String component;
}
