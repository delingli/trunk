package com.hkzr.wlwd.model;

/**
 * Created by admin on 2017/8/25.
 */

public class CalendarDetailBean {
    private AddCalendarBean Model;
    private String Limit;

    public AddCalendarBean getModel() {
        return Model;
    }

    public void setModel(AddCalendarBean model) {
        Model = model;
    }

    public String getLimit() {
        return Limit;
    }

    public void setLimit(String limit) {
        Limit = limit;
    }
}
