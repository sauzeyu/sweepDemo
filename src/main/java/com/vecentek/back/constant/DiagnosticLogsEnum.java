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

    OPEN_THE_KEY_THERE_IS_A_FROZEN_KEY("04", "开通钥匙","6010","存在已冻结钥匙"),

    OPEN_THE_KEY_PROVISIONING_FAILED("04", "开通钥匙","6011","开通失败"),


    REVOKE_KEYS_REPLACE_PHONE_THERE_ARE_NULL_VALUES_IN_THE_PASSING_PARAMETERS("12", "吊销钥匙（更换手机）","5071","传参中存在空值"),

    REVOKE_KEYS_REPLACE_PHONE_THE_KEY_INFORMATION_IS_EMPTY("12", "吊销钥匙（更换手机）","5048","钥匙信息为空"),

    ENTER_VEHICLE_INFORMATION_AFTER_SALES_REPLACEMENT_THERE_ARE_NULL_VALUES_IN_THE_PASSING_PARAMETERS("0D", "录入车辆信息（售后换件）","5071","传参中存在空值"),

    ENTER_VEHICLE_INFORMATION_AFTER_SALES_REPLACEMENT_UPLOAD_DATA_THAT_EXCEEDS_THE_MAXIMUM("0D", "录入车辆信息（售后换件）","5050","上传数据超过最大值"),

    ENTER_VEHICLE_INFORMATION_AFTER_SALES_REPLACEMENT_DUPLICATE_UPLOAD_DATA("0D", "录入车辆信息（售后换件）","5045","上传数据重复"),

    USER_VEHICLE_BINDING_HERE_ARE_NULL_VALUES_IN_THE_PARAMETERS_OR_THE_PARAMETER_FORMAT_IS_INCORRECT("0E", "用户车辆绑定","5071","传参中存在空值或者参数格式不正确"),

    USER_VEHICLE_BINDING_VEHICLE_INFORMATION_DOES_NOT_EXIST("0E", "用户车辆绑定","5004","车辆信息不存在"),

    USER_VEHICLE_BINDING_THE_VEHICLE_INFORMATION_IS_BOUND_TO_THE_OWNER("0E", "用户车辆绑定","5045","车辆信息已绑定车主"),

    OWNER_CANCELLATION_UNBINDING_OF_PEOPLE_AND_VEHICLES_THERE_ARE_NULL_VALUES_IN_THE_PASSING_PARAMETERS("0F", "车主注销（解除人车绑定关系）","5071","传参中存在空值"),

    OWNER_CANCELLATION_UNBINDING_OF_PEOPLE_AND_VEHICLES_USER_VEHICLE_INFORMATION_DOES_NOT_MATCH("0F", "车主注销（解除人车绑定关系）","5046","用户车辆信息不匹配"),

    OWNER_CANCELLATION_UNBINDING_OF_PEOPLE_AND_VEHICLES_VEHICLE_INFORMATION_DOES_NOT_EXIST("0F", "车主注销（解除人车绑定关系）","5004","车辆信息不存在"),


    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_THERE_ARE_NULL_VALUES_IN_THE_PASSING_PARAMETERS("10", "分享钥匙（用户中心-钥匙平台）","5071","传参中存在空值"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_THE_KEY_TAKES_EFFECT_OR_THE_EXPIRATION_TIME_FORMAT_CANNOT_BE_PARSED("10", "分享钥匙（用户中心-钥匙平台）","5025","钥匙生效或失效时间格式解析失败"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_IT_IS_FORBIDDEN_TO_SHARE_WITH_YOURSELF("10", "分享钥匙（用户中心-钥匙平台）","504B","禁止自己分享给自己"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_THE_KEY_INFORMATION_IS_EMPT("10", "分享钥匙（用户中心-钥匙平台）","5048","钥匙信息为空"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_THE_KEY_STATUS_IS_ABNORMAL_AND_CANNOT_BE_SHARED("10", "分享钥匙（用户中心-钥匙平台）","504A","钥匙状态异常不能分享"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_THE_VEHICLE_INFORMATION_IS_EMPTY("10", "分享钥匙（用户中心-钥匙平台）","5004","车辆信息为空"),

    SHARE_KEYS_USER_CENTER_KEY_PLATFORM_BLUETOOTH_INFORMATION_DOES_NOT_HAVE_A_CORRESPONDING_SECONDARY_KEY("10", "分享钥匙（用户中心-钥匙平台）","504E","蓝牙信息没有对应二级密钥"),

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
    public static String getBusinessId(String business) {
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
    public static String getFaultId(String fault) {
        for (DiagnosticLogsEnum diagnosticLogsEnum : DiagnosticLogsEnum.values()) {
            if (diagnosticLogsEnum.fault.equalsIgnoreCase(fault)) {
                return diagnosticLogsEnum.getFaultId();
            }
        }
        return "未知操作";
    }

}