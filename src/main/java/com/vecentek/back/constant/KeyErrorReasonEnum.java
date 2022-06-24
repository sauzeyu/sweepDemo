package com.vecentek.back.constant;

import cn.hutool.core.util.StrUtil;

/**
 * @author Vectek
 */
public enum KeyErrorReasonEnum {


    /**
     * 车控失败原因
     */
    REASON_SUCCESS("00", "未知原因"),
    REASON_POWER_MODE_NOT_OFF("01", "电源模式非OFF"),
    REASON_VEHICLE_UNLOCKED("02", "车辆未上锁"),
    REASON_OTHER_COMMAND_IN_PROGRESS("03", "其他指令正在执行"),
    REASON_COMMAND_IN_PROGRESS("04", "指令正在执行"),
    REASON_UNKNOWN_COMMAND("06", "协议未定义的指令"),
    REASON_VEHICLE_NOT_FULLY_ARMED("0A", "车辆未设防"),
    REASON_LIGHTS_HAVE_FAILED("14", "灯系统失效"),
    REASON_DOOR_OPEN("15", "车门打开"),
    REASON_LOCKING_SYSTEM_FAILURE("16", "锁系统失效"),
    REASON_DOOR_IS_UNLOCKED("19", "车辆未解锁"),
    REASON_VEHICLE_ALARM_ACTIVE("1A", "车辆报警"),
    REASON_REQUEST_ID_IS_DIFFERENT("20", "车控ID前后不一致"),
    REASON_VEHICLE_SPEED_LIMIT("22", "车速限制"),
    REASON_HORN_DRIVER_FAILURE("31", "喇叭驱动失效"),
    REASON_AUTH_FAIL("3B", "认证失败"),
    REASON_REQUEST_PARAMETERS_OUT_OF_PROTOCOL("50", "指令参数超出协议"),
    REASON_NO_AUTHORITY("51", "没有权限"),
    REASON_CAN_TIMEOUT("52", "PEPS网络超时"),
    REASON_FUNCTION_TIMEOUT_FOR_UPDATE_STATUS("FE", "RKE网络超时"),
    REASON_FEATURE_REQUESTED_NOT_SUPPORTED("FF", "功能不支持");


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