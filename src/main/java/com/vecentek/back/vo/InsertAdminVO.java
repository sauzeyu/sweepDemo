package com.vecentek.back.vo;

import lombok.Data;

@Data
public class InsertAdminVO {
    private String username;
    private String password;
    private String extraInfo;
    private Integer[] role;
}
