package com.hkzr.wlwd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2017/8/22.
 */

public class CalendarTypeBean implements Parcelable {
    public CalendarTypeBean() {
    }

    public CalendarTypeBean(String calID, String calTitle, String calType) {
        CalID = calID;
        CalTitle = calTitle;
        CalType = calType;
    }

    /**
     * CalID : 5d9dab6f-6a6a-4411-bf0b-9bdc9359d857
     * CalTitle : 新日程
     * CalType : 1
     */


    private String CalID;
    private String CalTitle;
    private String CalType;

    public String getCalID() {
        return CalID;
    }

    public void setCalID(String CalID) {
        this.CalID = CalID;
    }

    public String getCalTitle() {
        return CalTitle;
    }

    public void setCalTitle(String CalTitle) {
        this.CalTitle = CalTitle;
    }

    public String getCalType() {
        return CalType;
    }

    public void setCalType(String CalType) {
        this.CalType = CalType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.CalID);
        dest.writeString(this.CalTitle);
        dest.writeString(this.CalType);
    }

    protected CalendarTypeBean(Parcel in) {
        this.CalID = in.readString();
        this.CalTitle = in.readString();
        this.CalType = in.readString();
    }

    public static final Parcelable.Creator<CalendarTypeBean> CREATOR = new Parcelable.Creator<CalendarTypeBean>() {
        @Override
        public CalendarTypeBean createFromParcel(Parcel source) {
            return new CalendarTypeBean(source);
        }

        @Override
        public CalendarTypeBean[] newArray(int size) {
            return new CalendarTypeBean[size];
        }
    };
}
