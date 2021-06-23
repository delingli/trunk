package com.hkzr.wlwd.ui.widget.month;

/**
 * Created by admin on 2017/8/10.
 */

public class HolidayBean {
    private int dateNum;
    private int type;

    public HolidayBean() {
    }

    public HolidayBean(int dateNum, int type) {
        this.dateNum = dateNum;
        this.type = type;
    }

    public int getDateNum() {
        return dateNum;
    }

    public void setDateNum(int dateNum) {
        this.dateNum = dateNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HolidayBean that = (HolidayBean) o;

        if (dateNum != that.dateNum) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = dateNum;
        result = 31 * result + type;
        return result;
    }
}
