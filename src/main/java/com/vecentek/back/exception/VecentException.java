package com.vecentek.back.exception;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-04-19 20:25
 */

public class VecentException extends Exception {
    private Integer code = 500;

    public VecentException(String msg) {
        super(msg);
    }

    public VecentException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public VecentException(int code, String msg) {
        super(msg);
        this.code = code;
    }


    public Integer getCode() {
        return code;
    }
}
