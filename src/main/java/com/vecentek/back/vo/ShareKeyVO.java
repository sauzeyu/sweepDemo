package com.vecentek.back.vo;

import lombok.Data;

/**
 * 分享钥匙传参实体
 */
@Data
public class ShareKeyVO {
    //  车主用户ID
    private String userId;
    //  被分享用户ID（来源于TSP后台）
    private String shareUserId;
    //  被分享用户的设备指纹（来源于TSP后台，用户注册之后生成的设备指纹）
    private String phoneFingerprint;
    //  数字钥匙失效时间
    private String valTo;
    //  数字钥匙生效时间
    private String valFrom;
    //  车辆vin号（来源于车主钥匙信息）
    private String vin;
    //  车主钥匙ID（来源于车主钥匙信息）
    private String keyId;
    //  钥匙权限
    private Integer keyPermit;

}
