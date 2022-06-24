package com.vecentek.back.constant;

import cn.hutool.core.util.StrUtil;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-27 14:11
 */
public enum KeyStatusCodeEnum {
    /**
     * CMD 指令
     */
    SAFE_BLUETOOTH_DISCONNECT("08", "蓝牙断开"),

    SAFE_AUTOMATIC_WELCOME("0A", "自动迎宾"),

    SAFE_AUTOMATIC_LOCK_LEAVE("0B", "离车自动上锁"),

    SAFE_AUTOMATIC_UNLOCK_NEARBY("0C", "近车自动解锁"),

    SAFE_START_WITH_BLUETOOTH_KEY("0D", "通过蓝牙数字钥匙一键启动"),

    SAFE_VEHICLE_STILL_RUNNING("14", "车辆未熄火"),

    SAFE_VEHICLE_NOT_LOCKED("15", "车辆未闭锁"),

    SAFE_VEHICLE_NOT_POWERED_OFF("16", "车辆未下电"),

    SAFE_LOCK_BY_HANDLE_BUTTON("03", "把手按钮上锁"),

    SAFE_UNLOCK_BY_HANDLE_BUTTON("04", "把手按钮解锁"),

    SAFE_BUTTON_TRIGGERS_TRUNK_OPENING("09", "按钮触发开启后备箱"),

    SAFE_PHONE_AUTOMATICALLY_POPS("0E", "手机在车内车辆上锁手动关闭后备箱后自动弹开"),

    SAFE_KICK_OPEN_TAIL_DOOR("0F", "脚踢开启尾门"),

    SAFE_PHONE_IN_CAR_LAST_DOOR_REMIND("10", "手机在车内关闭最后一扇门提醒 "),

    SAFE_PHONE_IN_CAR_DOOR_CLOSED_VEHICLE_AUTOMATICALLY_UNLOCK("11", "手机在车内上锁后关闭车门车辆自动解锁"),

    SAFE_MULTIPLE_BLUETOOTH_WAKEUP_TIMES("12", "多蓝牙唤醒次数超限"),

    /**
     * 4.14 遥控指令 REMOTE
     */
    REMOTE_LOCK("B1", "车门锁"),

    REMOTE_TRUNK("B2", "后背箱"),

    REMOTE_ALARM("B3", "报警器"),

    REMOTE_ENGINE_START("B4", "引擎启动"),

    REMOTE_GENERAL("B5", "普通");

    /**
     * CMD 操作码
     */
    private final String code;
    /**
     * 操作名
     */
    private final String name;

    KeyStatusCodeEnum(String code, String name) {
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
     * 根据CMD操作码找到对应操作
     *
     * @param cmdCode 状态码
     * @return {@link String}
     */
    public static String matchName(String cmdCode) {
        if (StrUtil.isBlank(cmdCode)) {
            return "未知操作";
        }
        for (KeyStatusCodeEnum statusCode : KeyStatusCodeEnum.values()) {
            if (cmdCode.equals(statusCode.getCode())) {
                return statusCode.getName();
            }
        }
        return "未知操作";
    }

}