package com.vecentek.back.vo;

import lombok.Data;

@Data
public class KeyLogDataResVO {
    /**
     * 钥匙id
     */
    private String keyId;
    /**
     * 数字钥匙生效时间
     */
    private String valFrom;
    /**
     * 数字钥匙失效时间
     */
    private String valTo;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 申请时间
     */
    private String applyTime;
    /**
     * 是否激活；0未激活 1已激活
     */
    private Integer flag;
    /**
     * 数字钥匙的具体状态(1 已启用，3 冻结 ，4 过期， 5 吊销)；
     */
    private Integer dkState;
    /**
     * 父钥匙ID，车主钥匙为0
     */
    private String parentId;
    /**
     * 车架号
     */
    private String vin;
}
