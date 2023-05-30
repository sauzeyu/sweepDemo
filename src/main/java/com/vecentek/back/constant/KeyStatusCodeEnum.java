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

    SAFE_LOCK_BY_HANDLE_BUTTON("03", "把手按钮上锁"),

    SAFE_UNLOCK_BY_HANDLE_BUTTON("04", "把手按钮解锁"),

    SAFE_ENGINE_START("05", "引擎停止"),

    SAFE_ENGINE_STOP("06", "引擎启动"),

    SAFE_BUTTON_TRIGGERS_TRUNK_OPENING("09", "按钮触发开启后备箱"),

    SAFE_PHONE_AUTOMATICALLY_POPS("0E", "手机在车内车辆上锁手动关闭后备箱后自动弹开"),

    SAFE_KICK_OPEN_TAIL_DOOR("0F", "脚踢开启尾门"),

    SAFE_PHONE_IN_CAR_LAST_DOOR_REMIND("10", "手机在车内关闭最后一扇门提醒 "),

    SAFE_PHONE_IN_CAR_DOOR_CLOSED_VEHICLE_AUTOMATICALLY_UNLOCK("11", "手机在车内上锁后关闭车门车辆自动解锁"),

    SAFE_MULTIPLE_BLUETOOTH_WAKEUP_TIMES("12", "多蓝牙唤醒次数超限"),

    // 14-16 原因填充0x00
    SAFE_VEHICLE_STILL_RUNNING("14", "车辆未熄火"),

    SAFE_VEHICLE_NOT_LOCKED("15", "车辆未闭锁"),

    SAFE_VEHICLE_NOT_POWERED_OFF("16", "车辆未下电"),

    //  日志调试
    COMMAND_EXECUTED_SUCCESSFULLY("0x00", "指令执行成功"),

    COMMAND_EXECUTION_FAILED("0x01", "指令执行失败"),

    RESEND("0x02", "重发"),

    CLOCK_RESET("0x07", "时钟已重置"),

    BLUETOOTH_DISCONNECTED("0x08", "蓝牙断开"),

    /**
     * 4.14 遥控指令 REMOTE
     */
//    REMOTE_LOCK("B1", "车门锁"),
//
//    REMOTE_TRUNK("B2", "后背箱"),
//
//    REMOTE_ALARM("B3", "报警器"),
//
//    REMOTE_ENGINE_START("B4", "引擎启动"),
//
//    REMOTE_GENERAL("B5", "普通");

    // 2022.7.5 修改后 新加一个Tag byte
    SAFE_LOCK("0100", "解闭锁-解锁"),
    SAFE_UNLOCK("0101", "解闭锁-闭锁"),

    SAFE_FIND_CAR("0300", "寻车"),

    /**
     * 发动机
     */
    SAFE_SHUT_OFF_ENGINE("0400", "发动机-关闭"),
    SAFE_START_ENGINE_10M("0401", "发动机-启动(10分钟)"),
    SAFE_START_ENGINE_20M("0402", "发动机-启动(20分钟)"),
    SAFE_START_ENGINE_30M("0403", "发动机-启动(30分钟)"),

    /**
     * 空调
     */
    SAFE_SHUT_OFF_AIR("0500", "空调-关闭"),
    SAFE_START_AIR_AUTO_10M("0501", "空调-启动—自动(10分钟)"),
    SAFE_START_AIR_AUTO_20M("0502", "空调-启动—自动(20分钟)"),
    SAFE_START_AIR_AUTO_30M("0503", "空调-启动—自动(30分钟)"),
    SAFE_START_AIR_LOW_10M("0504", "空调-启动—低(10分钟)"),
    SAFE_START_AIR_LOW_20M("0505", "空调-启动—低(20分钟)"),
    SAFE_START_AIR_LOW_30M("0506", "空调-启动—低(30分钟)"),
    SAFE_START_AIR_HIGH_10M("0507", "空调-启动—高(10分钟)"),
    SAFE_START_AIR_HIGH_20M("0508", "空调-启动—高(20分钟)"),
    SAFE_START_AIR_HIGH_30M("0509", "空调-启动—高(30分钟)"),
    SAFE_START_AIR_DEFROST_10M("050A", "空调-启动—除霜(10分钟)"),
    SAFE_START_AIR_DEFROST_20M("050B", "空调-启动—除霜(20分钟)"),
    SAFE_START_AIR_DEFROST_30M("050C", "空调-启动—除霜(30分钟)"),

    /**
     * 车窗
     */
    SAFE_DRIVER_WINDOWS_CLOSE("0600", "司机车窗—关闭"),
    SAFE_DRIVER_WINDOWS_OPEN_25("0601", "司机车窗—打开(25%)"),
    SAFE_DRIVER_WINDOWS_OPEN_50("0602", "司机车窗—打开(50%)"),
    SAFE_DRIVER_WINDOWS_OPEN_75("0603", "司机车窗—打开(75%)"),
    SAFE_DRIVER_WINDOWS_OPEN_100("0604", "司机车窗—打开(100%)"),

    SAFE_PASSENGER_WINDOW_CLOSE("0700", "副驾驶车窗—关闭"),
    SAFE_PASSENGER_WINDOW_OPEN_25("0701", "副驾驶车窗—打开(25%)"),
    SAFE_PASSENGER_WINDOW_OPEN_50("0702", "副驾驶车窗—打开(50%)"),
    SAFE_PASSENGER_WINDOW_OPEN_75("0703", "副驾驶车窗—打开(75%)"),
    SAFE_PASSENGER_WINDOW_OPEN_100("0704", "副驾驶车窗—打开(100%)"),

    SAFE_LEFT_REAR_WINDOW_CLOSE("0800", "左后门车窗—关闭"),
    SAFE_LEFT_REAR_WINDOW_OPEN_25("0801", "左后门车窗—打开(25%)"),
    SAFE_LEFT_REAR_WINDOW_OPEN_50("0802", "左后门车窗—打开(50%)"),
    SAFE_LEFT_REAR_WINDOW_OPEN_75("0803", "左后门车窗—打开(75%)"),
    SAFE_LEFT_REAR_WINDOW_OPEN_100("0804", "左后门车窗—打开(100%)"),

    SAFE_RIGHT_REAR_WINDOW_CLOSE("0900", "右后门车窗—关闭"),
    SAFE_RIGHT_REAR_WINDOW_OPEN_25("0901", "右后门车窗—打开(25%)"),
    SAFE_RIGHT_REAR_WINDOW_OPEN_50("0902", "右后门车窗—打开(50%)"),
    SAFE_RIGHT_REAR_WINDOW_OPEN_75("0903", "右后门车窗—打开(75%)"),
    SAFE_RIGHT_REAR_WINDOW_OPEN_100("0904", "右后门车窗—打开(100%)"),

    /**
     * 遮阳罩
     */
    SAFE_SUN_SHADE_CLOSE("0A00", "遮阳罩—关闭"),
    SAFE_SUN_SHADE_OPEN("0A01", "遮阳罩—打开"),

    /**
     * 主驾通风加热
     */
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_CLOSE("0C00", "座椅通风加热—关闭主驾驶座椅控制"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_VENTILATE_HIGH_10M("0C01", "座椅通风加热—启动主驾座椅—通风-高(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_VENTILATE_HIGH_20M("0C02", "座椅通风加热—启动主驾座椅—通风-高(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_VENTILATE_HIGH_30M("0C03", "座椅通风加热—启动主驾座椅—通风-高(30分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_VENTILATE_MIDDLE_10M("0C04", "座椅通风加热—启动主驾座椅—通风-中(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_VENTILATE_MIDDLE_20M("0C05", "座椅通风加热—启动主驾座椅—通风-中(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_VENTILATE_MIDDLE_30M("0C06", "座椅通风加热—启动主驾座椅—通风-中(30分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_VENTILATE_LOW_10M("0C07", "座椅通风加热—启动主驾座椅—通风-低(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_VENTILATE_LOW_20M("0C08", "座椅通风加热—启动主驾座椅—通风-低(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_VENTILATE_LOW_30M("0C09", "座椅通风加热—启动主驾座椅—通风-低(30分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_HEAT_HIGH_10M("0C0A", "座椅通风加热—启动主驾座椅—加热-高(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_HEAT_HIGH_20M("0C0B", "座椅通风加热—启动主驾座椅—加热-高(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_HEAT_HIGH_30M("0C0C", "座椅通风加热—启动主驾座椅—加热-高(30分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_HEAT_MIDDLE_10M("0C0D", "座椅通风加热—启动主驾座椅—加热-中(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_HEAT_MIDDLE_20M("0C0E", "座椅通风加热—启动主驾座椅—加热-中(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_HEAT_MIDDLE_30M("0C0F", "座椅通风加热—启动主驾座椅—加热-中(30分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_HEAT_LOW_10M("0C10", "座椅通风加热—启动主驾座椅—加热-低(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_HEAT_LOW_20M("0C11", "座椅通风加热—启动主驾座椅—加热-低(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_DRIVER_HEAT_LOW_30M("0C12", "座椅通风加热—启动主驾座椅—加热-低(30分钟)"),

    /**
     * 副驾通风加热
     */
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_CLOSE("0C13", "座椅通风加热—关闭副驾驶座椅控制"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_VENTILATE_HIGH_10M("0C14", "座椅通风加热—启动副驾座椅—通风-高(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_VENTILATE_HIGH_20M("0C15", "座椅通风加热—启动副驾座椅—通风-高(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_VENTILATE_HIGH_30M("0C16", "座椅通风加热—启动副驾座椅—通风-高(30分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_VENTILATE_MIDDLE_10M("0C17", "座椅通风加热—启动副驾座椅—通风-中(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_VENTILATE_MIDDLE_20M("0C18", "座椅通风加热—启动副驾座椅—通风-中(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_VENTILATE_MIDDLE_30M("0C19", "座椅通风加热—启动副驾座椅—通风-中(30分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_VENTILATE_LOW_10M("0C1A", "座椅通风加热—启动副驾座椅—通风-低(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_VENTILATE_LOW_20M("0C1B", "座椅通风加热—启动副驾座椅—通风-低(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_VENTILATE_LOW_30M("0C1C", "座椅通风加热—启动副驾座椅—通风-低(30分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_HEAT_HIGH_10M("0C1D", "座椅通风加热—启动副驾座椅—加热-高(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_HEAT_HIGH_20M("0C1E", "座椅通风加热—启动副驾座椅—加热-高(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_HEAT_HIGH_30M("0C1F", "座椅通风加热—启动副驾座椅—加热-高(30分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_HEAT_MIDDLE_10M("0C20", "座椅通风加热—启动副驾座椅—加热-中(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_HEAT_MIDDLE_20M("0C21", "座椅通风加热—启动副驾座椅—加热-中(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_HEAT_MIDDLE_30M("0C22", "座椅通风加热—启动副驾座椅—加热-中(30分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_HEAT_LOW_10M("0C23", "座椅通风加热—启动副驾座椅—加热-低(10分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_HEAT_LOW_20M("0C24", "座椅通风加热—启动副驾座椅—加热-低(20分钟)"),
    SAFE_SEAT_VENTILATION_HEATING_PASSENGER_HEAT_LOW_30M("0C25", "座椅通风加热—启动副驾座椅—加热-低(30分钟)"),

    SAFE_TRUNK_UNLOCK("0D00", "后备箱解锁"),
    SAFE_TRUNK_LOCK("0D01", "后备箱闭锁");

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

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}