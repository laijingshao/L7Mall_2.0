package com.ls.l7mall_2.global;

/**
 * @author laijs
 * @date 2020-3-28-11:53
 */
public enum ResponseCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String decs;


    ResponseCode(int code, String decs) {
        this.code = code;
        this.decs = decs;
    }

    public int getCode() {
        return code;
    }

    public String getDecs() {
        return decs;
    }
}
