package com.vecentek.back.constant;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-27 14:11
 */
public enum KeyStatusEnum {

    /**
     * 数字钥匙的具体状态(1 已启用，3 冻结 ，4 过期， 5 吊销)
     */
    ACTIVATED(1, "已启用"),

    FREEZE(3, "冻结"),

    EXPIRED(4, "过期"),

    REVOKE(5, "吊销"),


    ;

    /**
     * CMD 操作码
     */
    private final Integer code;
    /**
     * 操作名
     */
    private final String name;

    KeyStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据CMD操作码找到对应操作
     *
     * @param cmdCode 状态码
     * @return {@link String}
     */
    public static String matchName(Integer cmdCode) {
        if (cmdCode == null) {
            return "未知操作";
        }
        for (KeyStatusEnum statusCode : KeyStatusEnum.values()) {
            if (cmdCode.equals(statusCode.getCode())) {
                return statusCode.getName();
            }
        }
        return "未知操作";
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}