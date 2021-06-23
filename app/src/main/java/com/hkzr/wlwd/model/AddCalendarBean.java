package com.hkzr.wlwd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2017/8/24.
 */

public class AddCalendarBean implements Parcelable {
    private String EventId;
    private String CalendarId;
    private String CalendarName;
    private String Subject;
    private String IsDayEvent;
    private String EventType;
    private String StartTime;
    private String EndTime;
    private String Repeat;
    private String REndDate;
    private String AlertSet;
    private String Location;
    private String Coords;
    private String Description;
    private String IsPrivate;
    private String TimeInfo;
    private String ExcepType ;

    public AddCalendarBean(String eventId, String calendarId, String calendarName, String subject, String isDayEvent, String eventType, String startTime, String endTime, String repeat, String REndDate, String alertSet, String location, String coords, String description, String isPrivate) {
        EventId = eventId;
        CalendarId = calendarId;
        CalendarName = calendarName;
        Subject = subject;
        IsDayEvent = isDayEvent;
        EventType = eventType;
        StartTime = startTime;
        EndTime = endTime;
        Repeat = repeat;
        this.REndDate = REndDate;
        AlertSet = alertSet;
        Location = location;
        Coords = coords;
        Description = description;
        IsPrivate = isPrivate;
    }

    public String getCalendarName() {
        return CalendarName;
    }

    public void setCalendarName(String calendarName) {
        CalendarName = calendarName;
    }

    public String getTimeInfo() {
        return TimeInfo;
    }

    public void setTimeInfo(String timeInfo) {
        TimeInfo = timeInfo;
    }

    public AddCalendarBean() {
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getCalendarId() {
        return CalendarId;
    }

    public void setCalendarId(String calendarId) {
        CalendarId = calendarId;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getIsDayEvent() {
        return IsDayEvent;
    }

    public void setIsDayEvent(String isDayEvent) {
        IsDayEvent = isDayEvent;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getRepeat() {
        return Repeat;
    }

    public void setRepeat(String repeat) {
        Repeat = repeat;
    }

    public String getREndDate() {
        return REndDate;
    }

    public void setREndDate(String REndDate) {
        this.REndDate = REndDate;
    }

    public String getAlertSet() {
        return AlertSet;
    }

    public void setAlertSet(String alertSet) {
        AlertSet = alertSet;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getCoords() {
        return Coords;
    }

    public void setCoords(String coords) {
        Coords = coords;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIsPrivate() {
        return IsPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        IsPrivate = isPrivate;
    }

    public String getExcepType() {
        return ExcepType;
    }

    public void setExcepType(String excepType) {
        ExcepType = excepType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EventId);
        dest.writeString(this.CalendarId);
        dest.writeString(this.CalendarName);
        dest.writeString(this.Subject);
        dest.writeString(this.IsDayEvent);
        dest.writeString(this.EventType);
        dest.writeString(this.StartTime);
        dest.writeString(this.EndTime);
        dest.writeString(this.Repeat);
        dest.writeString(this.REndDate);
        dest.writeString(this.AlertSet);
        dest.writeString(this.Location);
        dest.writeString(this.Coords);
        dest.writeString(this.Description);
        dest.writeString(this.IsPrivate);
        dest.writeString(this.TimeInfo);
        dest.writeString(this.ExcepType);
    }

    protected AddCalendarBean(Parcel in) {
        this.EventId = in.readString();
        this.CalendarId = in.readString();
        this.CalendarName = in.readString();
        this.Subject = in.readString();
        this.IsDayEvent = in.readString();
        this.EventType = in.readString();
        this.StartTime = in.readString();
        this.EndTime = in.readString();
        this.Repeat = in.readString();
        this.REndDate = in.readString();
        this.AlertSet = in.readString();
        this.Location = in.readString();
        this.Coords = in.readString();
        this.Description = in.readString();
        this.IsPrivate = in.readString();
        this.TimeInfo = in.readString();
        this.ExcepType = in.readString();
    }

    public static final Parcelable.Creator<AddCalendarBean> CREATOR = new Parcelable.Creator<AddCalendarBean>() {
        @Override
        public AddCalendarBean createFromParcel(Parcel source) {
            return new AddCalendarBean(source);
        }

        @Override
        public AddCalendarBean[] newArray(int size) {
            return new AddCalendarBean[size];
        }
    };
}
