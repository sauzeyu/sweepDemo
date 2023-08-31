package com.vecentek.back.constant;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-27 14:11
 */
public enum KeyErrorTypeEnum {

    /**
     * 数字钥匙的具体状态(01 控车，02 蓝牙连接 ，03 钥匙管理)
     */
    CAR_CONTROL("01", "控车"),

    BLUETOOTH_CONNECTION("02", "蓝牙连接"),

    KEY_MANAGEMENT("03", "钥匙管理"),



    ;

    /**
     * CMD 操作码
     */
    private final String code;
    /**
     * 操作名
     */
    private final String name;

    KeyErrorTypeEnum(String code, String name) {
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
        if (cmdCode == null) {
            return "未知操作";
        }
        for (KeyErrorTypeEnum statusCode : KeyErrorTypeEnum.values()) {
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