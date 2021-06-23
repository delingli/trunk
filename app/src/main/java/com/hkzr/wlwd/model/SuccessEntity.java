package com.hkzr.wlwd.model;

/**
 * Created by admin on 2017/6/14.
 */

public class SuccessEntity {


    /**
     * code : 200
     * errorMessage : null
     */

    private int code;
    private Object errorMessage;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }
}
