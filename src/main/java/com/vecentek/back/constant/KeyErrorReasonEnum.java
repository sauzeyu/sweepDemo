package com.vecentek.back.constant;

import cn.hutool.core.util.StrUtil;

/**
 * @author Vectek
 */
public enum KeyErrorReasonEnum {


    /**
     * 车控失败原因 福田项目未使用
     */
//    REASON_SUCCESS("00", "未知原因"),
//    REASON_POWER_MODE_NOT_OFF("01", "电源模式非OFF"),
//    REASON_VEHICLE_UNLOCKED("02", "车辆未上锁"),
//    REASON_OTHER_COMMAND_IN_PROGRESS("03", "其他指令正在执行"),
//    REASON_COMMAND_IN_PROGRESS("04", "指令正在执行"),
//    REASON_UNKNOWN_COMMAND("06", "协议未定义的指令"),
//    REASON_VEHICLE_NOT_FULLY_ARMED("0A", "车辆未设防"),
//    REASON_LIGHTS_HAVE_FAILED("14", "灯系统失效"),
//    REASON_DOOR_OPEN("15", "车门打开"),
//    REASON_LOCKING_SYSTEM_FAILURE("16", "锁系统失效"),
//    REASON_DOOR_IS_UNLOCKED("19", "车辆未解锁"),
//    REASON_VEHICLE_ALARM_ACTIVE("1A", "车辆报警"),
//    REASON_REQUEST_ID_IS_DIFFERENT("20", "车控ID前后不一致"),
//    REASON_VEHICLE_SPEED_LIMIT("22", "车速限制"),
//    REASON_HORN_DRIVER_FAILURE("31", "喇叭驱动失效"),
//    REASON_AUTH_FAIL("3B", "认证失败"),
//    REASON_REQUEST_PARAMETERS_OUT_OF_PROTOCOL("50", "指令参数超出协议"),
//    REASON_NO_AUTHORITY("51", "没有权限"),
//    REASON_CAN_TIMEOUT("52", "PEPS网络超时"),
//    REASON_FUNCTION_TIMEOUT_FOR_UPDATE_STATUS("FE", "RKE网络超时"),
//    REASON_FEATURE_REQUESTED_NOT_SUPPORTED("FF", "功能不支持");

    /**
     *  车端SDK定义
     */
    ERROR_REASON_42("42", "验证KR失败"),
    ERROR_REASON_45("45", "随机数校验失败"),
    ERROR_REASON_4B("4B", "钥匙已过期"),
    ERROR_REASON_4A("4A", "钥匙未开启"),
    ERROR_REASON_01("01", "认证超时"),
    ERROR_REASON_02("02", "认证失败"),
    ERROR_REASON_03("03", "版本协商失败,请升级APP版本"),
    ERROR_REASON_04("04", "离线可用次数为0,请同步钥匙"),
    ERROR_REASON_05("05", "激活超时"),
    ERROR_REASON_06("06", "激活失败"),
    ERROR_REASON_07("07", "RTC掉电同步失败,请到有网环境进行同步"),
    ERROR_REASON_08("08", "认证超时"),
    ERROR_REASON_09("09", "白名单认证失败,请到有网环境进行同步"),
    ERROR_REASON_0A("0A", "新的车主接入"),
    ERROR_REASON_0B("0B", "用户连接数已满,非车主钥匙无法进行连接"),
    ERROR_REASON_0C("0C", "白名单冻结,请到有网环境进行同步"),
    ERROR_REASON_0D("0D", "软件版本不兼容,请升级车端软件"),
    ERROR_REASON_0F("0F", "车端连接数已满,有车主接入"),

    /**
     *  IOS特有
     *
     */
    ERROR_REASON_C2("C2", "无效操作"),
    ERROR_REASON_C3("C3", "未连接"),
    ERROR_REASON_C4("C4", "离开范围"),
    ERROR_REASON_C5("C5", "操作取消"),
    ERROR_REASON_C6("C6", "连接超时"),
    ERROR_REASON_C7("C7", "设备断开"),
    ERROR_REASON_CA("CA", "连接失败"),
    ERROR_REASON_CB("CB", "连接达到限制数量"),
    ERROR_REASON_CC("CC", "操作不支持"),
    ERROR_REASON_CE("CE", "丢失配对信息"),
    ERROR_REASON_CF("CF", "加密超时"),
    ERROR_REASON_D0("D0", "LE配对设备过多"),

    /**
     *  Android特有
     *
     */
    ERROR_REASON_D1("D1", "异常断开");


    /**
     * CMD 操作码
     */
    private final String code;
    /**
     * 操作名
     */
    private final String name;

    KeyErrorReasonEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
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
        for (KeyErrorReasonEnum reason : KeyErrorReasonEnum.values()) {
            if (cmdCode.equals(reason.getCode())) {
                return reason.getName();
            }
        }
        return "未知原因";
    }
}