package com.hkzr.wlwd.model;

import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class CalendarBean {

    /**
     * AdminCals : [{"CalID":"5d9dab6f-6a6a-4411-bf0b-9bdc9359d857","CalTitle":"新日程","CalType":"1"}]
     * CalendarId : 5d9dab6f-6a6a-4411-bf0b-9bdc9359d857
     * DayList : [{"IsActive":"0"},{"Flag":"1","IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"Flag":"2","IsActive":"0"},{"IsActive":"0"},{"Flag":"1","IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"Flag":"2","IsActive":"0"},{"IsActive":"0"},{"Flag":"1","IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"Flag":"2","IsActive":"0"},{"IsActive":"0"},{"Flag":"1","IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"Flag":"2","IsActive":"0"},{"IsActive":"0"},{"Flag":"1","IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"Flag":"2","IsActive":"0"},{"IsActive":"0"},{"Flag":"1","IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"IsActive":"0"},{"Flag":"2","IsActive":"0"}]
     */

    private String CalendarId;
    private List<AdminCalsBean> AdminCals;
    private List<DayListBean> DayList;

    public String getCalendarId() {
        return CalendarId;
    }

    public void setCalendarId(String CalendarId) {
        this.CalendarId = CalendarId;
    }

    public List<AdminCalsBean> getAdminCals() {
        return AdminCals;
    }

    public void setAdminCals(List<AdminCalsBean> AdminCals) {
        this.AdminCals = AdminCals;
    }

    public List<DayListBean> getDayList() {
        return DayList;
    }

    public void setDayList(List<DayListBean> DayList) {
        this.DayList = DayList;
    }

    public static class AdminCalsBean {
        public AdminCalsBean(String calID, String calTitle, String calType) {
            CalID = calID;
            CalTitle = calTitle;
            CalType = calType;
        }

        public AdminCalsBean() {
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
    }

    public static class DayListBean {
        /**
         * IsActive : 0
         * Flag : 1
         */

        private String IsActive;
        private String Flag;
        private String Info;

        public String getInfo() {
            return Info;
        }

        public void setInfo(String info) {
            Info = info;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getFlag() {
            return Flag;
        }

        public void setFlag(String Flag) {
            this.Flag = Flag;
        }
    }
}
