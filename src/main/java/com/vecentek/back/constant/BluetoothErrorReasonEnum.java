package com.vecentek.back.constant;

import cn.hutool.core.util.StrUtil;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-27 15:23
 */
public enum BluetoothErrorReasonEnum {
    /**
     * 蓝牙断开原因
     */
//    最新车端文档更新：
//    {0x01, "认证超时"},
//    {0x02, "认证失败"},
//    {0x03, "版本协商失败,请升级APP版本"},
//    {0x04, "离线可用次数为0,请同步钥匙"},
//    {0x05, "激活超时"},
//    {0x06, "激活失败"},
//    {0x07, "RTC掉电同步,请到有网环境进行同步"},
//    {0x08, "认证超时"},
//    {0x09, "白名单认证失败,请到有网环境进行同步"},
//    {0x0A, "新的车主接入"},
//    {0x0B, "用户连接数已满,非车主钥匙无法进行连接"},
//    {0x0C, "白名单冻结,请到有网环境进行同步"},
//    {0x0D, "软件版本不兼容,请升级车端软件"},
//    {0x0F, "车端连接数已满,有车主接入"},
    BLUETOOTH_REASON_AUTHENTICATION_TIMEOUT("01", "认证超时"),
    BLUETOOTH_REASON_AUTHENTICATION_FAILS_THREE_TIMES("02", "连续3次认证失败"),
    BLUETOOTH_REASON_VERSION_NEGOTIATION_FAILS("03", "版本协商失败断开蓝牙"),
    BLUETOOTH_REASON_AVAILABLE_OFFLINE_TIMES_IS_ZERO("04", "离线可用次数为0"),
    BLUETOOTH_REASON_ACTIVATION_STARTED_30_SECONDS_LATER("05", "激活开始超过30S未完成"),
    BLUETOOTH_REASON_ACTIVATION_FAILED_THREE_TIMES("06", "连续3次激活失败"),
    BLUETOOTH_REASON_NON_OWNER_FAILED_TO_RTC_FOR_30_MINUTES("07", "非车主30min未成功同步时钟"),
    BLUETOOTH_REASON_AUTHENTICATION_TIMEOUT_DISCONNECTION("08", "蓝牙连接建立后1min未完成业务层认证"),
    BLUETOOTH_REASON_NOT_IN_WHITELIST("09", "不在白名单中的钥匙需要后台签名后加入白名单才能正常使用"),
    BLUETOOTH_REASON_NEW_OWNER_ACCESS("0A", "已有车主连接接入一路新车主断开已有的车主连接"),
    BLUETOOTH_REASON_AFTER_ACCESS_THE_NON_OWNER_DISCONNECTED("0B", "车端已有连接后面的非车主连接将会被断开"),
    BLUETOOTH_REASON_WHITELIST_FREEZE("0C", "白名单被冻结后车端会请求白名单验证如果手机不能及时提供白名单车端会断开连接"),
    BLUETOOTH_REASON_SOFTWARE_VERSIONS_INCOMPATIBLE("0D", "车端和手机版本协商失败"),
    BLUETOOTH_REASON_CONNECTIONS_FULLED("0F", "车端连接数已满有车主接入");
    /**
     * CMD 操作码
     */
    private final String code;
    /**
     * 错误原因
     */
    private final String reason;

    BluetoothErrorReasonEnum(String code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    /**
     * 根据CMD操作码找到对应原因
     *
     * @param cmdCode 状态码
     * @return {@link String}
     */
    public static String matchReason(String cmdCode) {
        if (StrUtil.isBlank(cmdCode)) {
            return "未知原因";
        }
        for (BluetoothErrorReasonEnum reason : BluetoothErrorReasonEnum.values()) {
            if (cmdCode.equals(reason.getCode())) {
                return reason.getReason();
            }
        }
        return "未知原因";
    }

    public String getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

}
