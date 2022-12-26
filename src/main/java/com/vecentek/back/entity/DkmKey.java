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
@EqualsAndHashCode()
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkmKey  implements Serializable {

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
     * 离线可用操作次数
     */
    private Integer activateTimes;

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

        /**
         * 钥匙分类，icce或者ccc
         */
        private Integer keyClassification;

        /**
         * 钥匙类型，车主或者非车主
         */
        private String keyType;

        /**
         * 手机设备类型
         */
        private String deviceType;

        /**
         * 设备OEM账户ID的hash
         */
        private String accountIdHash;

        /**
         * 用于标识数字钥匙，在创建DK时会用到，由device进行管理和生成
         */
        private String endpointId;

        /**
         * 由device在创建Instance CA时生成，用于标识签名当前DK所用的Instance CA
         */
        private String slotId;

        /**
         * 标识当前DK允许执行的交互类型（fast/standard，或者通过有线接口进行交互），并不属于访问权限类
         */
        private String keyOptions;

        /**
         * 在standard交互中使用，具有唯一性，在创建endpoint时生成，并存储在车端。存放在endpoint certificate的subjectPublicKey字段中，不等于Device.Enc.PK
         */
        private String devicePublicKey;

        /**
         * 在standard交互中使用，针对绑定到同一车辆的所有device都是用同一对公私钥对
         */
        private String vehiclePublicKey;

        /**
         * 由车端提供，在分享钥匙校验校验朋友公钥证书链时作为根证书
         */
        private String authorizedPublicKeys;

        /**
         * 一段缓存区，在NFC交互过程中可以传输给车端，或者由车端写入
         */
        private String privateMailbox;


        /**
         * 一段缓存区，不允许在NFC交互过程中传输给车端，或者由车端写入
         */
        private String confidentialMailbox;


        /**
         * 由朋友device创建，
         */
        private String friendDeviceHandle;

        /**
         * 由朋友device创建，
         */
        private String friendPublicKey;

        /**
         * 包含用于车端生成分享密码的种子。还包括车主device关于车辆在朋友激活分享钥匙时是否要求提供分享密码的策略
         */
        private String sharingPasswordInformation;

        /**
         * 由钥匙分享的发起者选择
         */
        private String profile;


        /**
         * 分享对象的名称。可以是分享时车主选择的联系人，需要支持车主编辑
         */
        private String keyFriendlyName;


}