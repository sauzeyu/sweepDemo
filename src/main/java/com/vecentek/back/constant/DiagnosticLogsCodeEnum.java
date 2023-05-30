package com.vecentek.back.constant;

import cn.hutool.core.util.StrUtil;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-27 14:11
 */
public enum DiagnosticLogsCodeEnum {
    /**
     * CMD 指令
     */
    CRITICAL_BUSINESS_INFO("01", "关键信息"),

    DK_COMM_ABNORMAL("02", "钥匙通信异常"),

    FAULT_HANDLER_RECORD("03", "异常处理记录");

    /**
     * CMD 操作码
     */
    private final String code;
    /**
     * 操作名
     */
    private final String name;

    DiagnosticLogsCodeEnum(String code, String name) {
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
        for (DiagnosticLogsCodeEnum statusCode : DiagnosticLogsCodeEnum.values()) {
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