package com.vecentek.back.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

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
public class DkmKey extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 559081210876573507L;

    /**
     * 钥匙id
     */
    private String id;
    /**
     * 车辆ID
     */
    private Integer vehicleId;
    /**
     * 钥匙用户ID
     */
    private String userId;

    /**
     * 车架号
     */
    private String vin;
    /**
     * 数字钥匙的具体状态(1：已启用 3：冻结 4：过期 5：吊销)
     */
    private Integer dkState;
    /**
     * 数字钥匙生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date valFrom;
    /**
     * 数字钥匙失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date valTo;
    /**
     * 签名值: 用服务端私钥对信息( server_cnt+user_id+手机设备指纹+VIN号+key_id+dk_state+val_from+val_to+permit+server_info) 做RSA2048的SHA256位的签名算法，得到签名值，转成Base64字符串，320位
     */
    private String serverSig;
    /**
     * 离线可用操作次数
     */
    private Integer activateTimes;
    /**
     * 随机数
     */
    private String random;
    /**
     * 钥匙信息kr
     */
    private String kr;
    /**
     * 一级密钥
     */
    private String ks;
    /**
     * 手机设备指纹
     */
    private String phoneFingerprint;
    /**
     * 授权权限值:  1 前门 2 后门 4 尾箱 8 启动 16车机
     */
    private Integer permissions;
    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;
    /**
     * 父钥匙ID
     */
    private String parentId;
    /**
     * 钥匙激活状态:0未激活 1已激活，用于微信链接分享钥匙
     */
    private Integer flag;

    private String personalAndCalibration;
    /**
     * 有效期到失效期分钟间隔
     */
    private Long period;

    /**
     * 钥匙来源（1：APP,2：小程序）
     */
    private Integer keyResource;

    /**
     * 钥匙来源（1：APP,2：小程序） excelIO对象
     */
    @TableField(exist = false)
    private String keyResourceVO;
}