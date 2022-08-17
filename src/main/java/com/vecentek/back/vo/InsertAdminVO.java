package com.vecentek.back.vo;

import lombok.Data;

import java.util.Date;

@Data
public class InsertAdminVO {
    private String username;
    private String password;
    private String extraInfo;
    private String creator;
    private Date createTime;
    private Integer roleId;

}
