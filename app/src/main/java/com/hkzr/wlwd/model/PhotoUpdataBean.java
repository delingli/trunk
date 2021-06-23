package com.hkzr.wlwd.model;

/**
 * Created by admin on 2017/6/26.
 */

public class PhotoUpdataBean {
    private String data;
    private String fmt;

    public PhotoUpdataBean() {
    }

    public PhotoUpdataBean(String data, String fmt) {
        this.data = data;
        this.fmt = fmt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFmt() {
        return fmt;
    }

    public void setFmt(String fmt) {
        this.fmt = fmt;
    }
}
