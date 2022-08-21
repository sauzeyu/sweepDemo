package com.vecentek.back.vo;

import com.vecentek.back.entity.DkmAdmin;
import lombok.Data;

import java.util.Date;

@Data
public class InsertAdminVO extends DkmAdmin {
    private Integer roleId;

}
