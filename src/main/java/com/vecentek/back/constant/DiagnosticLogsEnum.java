package com.vecentek.back.constant;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-27 14:11
 */
public enum DiagnosticLogsEnum {

    /**
     * 故障汇总的具体状态
     */

    OPEN_KEY_THERE_FROZEN_KEY("04", "开通钥匙","6010","存在已冻结钥匙"),

    OPEN_KEY_PROVISIONING_FAILED("04", "开通钥匙","6011","开通失败"),


    REVOKE_KEYS_REPLACE_PHONE_PARAMETERS_NULL("12", "吊销钥匙（更换手机）","5071","传参中存在空值"),

    REVOKE_KEYS_REPLACE_PHONE_KEY_NULL("12", "吊销钥匙（更换手机）","5048","钥匙信息为空"),


    ENTER_VEHICLE_REPLACEMENTTHE_PARAMETERS_NULL("0D", "录入车辆信息（售后换件）","5071","传参中存在空值"),

    ENTER_VEHICLE_REPLACEMENT_UPLOAD_MAXIMUM("0D", "录入车辆信息（售后换件）","5050","上传数据超过最大值"),

    ENTER_VEHICLE_REPLACEMENT_UPLOAD_REPEAT("0D", "录入车辆信息（售后换件）","5045","上传数据重复"),


    USER_VEHICLE_BINDING_PARAMETERS_NULL_OR_UNFORMAT("0E", "用户车辆绑定","5071","传参中存在空值或者参数格式不正确"),

    USER_VEHICLE_BINDING_VEHICLE_NOT_EXIST("0E", "用户车辆绑定","5004","车辆信息不存在"),

    USER_VEHICLE_BINDING_THE_VEHICLE_BOUND_OWNER("0E", "用户车辆绑定","5045","车辆信息已绑定车主"),


    OWNER_CANCELLATION_UNBINDING_PARAMETERS_NULL("0F", "车主注销（解除人车绑定关系）","5071","传参中存在空值"),

    OWNER_CANCELLATION_UNBINDING_USER_VEHICLE_NOT_MATCH("0F", "车主注销（解除人车绑定关系）","5046","用户车辆信息不匹配"),

    OWNER_CANCELLATION_UNBINDING_VEHICLE_INFORMATION_NOT_EXIST("0F", "车主注销（解除人车绑定关系）","5004","车辆信息不存在"),


    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_PARAMETERS_NULL("10", "分享钥匙（用户中心-钥匙平台）","5071","传参中存在空值"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_KEY_EFFECT_OR_TIME_UNFORMAT("10", "分享钥匙（用户中心-钥匙平台）","5025","钥匙生效或失效时间格式解析失败"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_SHARE_YOURSELF("10", "分享钥匙（用户中心-钥匙平台）","504B","禁止自己分享给自己"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_KEY_NULL("10", "分享钥匙（用户中心-钥匙平台）","5048","钥匙信息为空"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_KEY_ABNORMAL_AND_CANNOT_SHARED("10", "分享钥匙（用户中心-钥匙平台）","504A","钥匙状态异常不能分享"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_VEHICLE_INFORMATION_IS_EMPTY("10", "分享钥匙（用户中心-钥匙平台）","5004","车辆信息为空"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_BLUETOOTH__NOT_SECONDARY_KEY("10", "分享钥匙（用户中心-钥匙平台）","504E","蓝牙信息没有对应二级密钥"),

    ;


    private final String businessId;

    private final String business;
    private final String faultId;
    private final String fault;

    DiagnosticLogsEnum(String businessId, String business, String faultId, String fault) {
        this.business = business;
        this.fault = fault;
        this.businessId = businessId;
        this.faultId = faultId;
    }

    public String getBusiness() {
        return business;
    }

    public String getFault() {
        return fault;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getFaultId() {
        return faultId;
    }


    /**
     * 根据业务找到对应业务ID
     *
     * @param business 业务
     * @return {@link String}
     */
    public static String matchBusinessId(String business) {
        for (DiagnosticLogsEnum diagnosticLogsEnum : DiagnosticLogsEnum.values()) {
            if (diagnosticLogsEnum.business.equalsIgnoreCase(business)) {
                return diagnosticLogsEnum.getBusinessId();
            }
        }
        return "未知操作";
    }

    /**
     * 根据故障找到对应故障ID
     *
     * @param fault 故障
     * @return {@link String}
     */
    public static String matchFaultId(String fault) {
        for (DiagnosticLogsEnum diagnosticLogsEnum : DiagnosticLogsEnum.values()) {
            if (diagnosticLogsEnum.fault.equalsIgnoreCase(fault)) {
                return diagnosticLogsEnum.getFaultId();
            }
        }
        return "未知操作";
    }

}