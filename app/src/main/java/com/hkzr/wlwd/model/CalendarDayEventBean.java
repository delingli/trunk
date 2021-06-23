package com.hkzr.wlwd.model;

import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class CalendarDayEventBean {

    /**
     * Groups : [{"GroupTitle":"XXX的日程安排","list":[{"Done":0,"EndTime":"16:00","EventId":"0f6a07ad-3b2d-4bc9-b952-d1ac0ad63a3c","EventType":"1","IsDayEvent":0,"StartTime":"15:00","SubTitle":"15:00 - 16:00","Subject":"约见客户"},{"Done":0,"EndTime":"11:00","EventId":"b2429296-b290-4079-85ac-ef96d859fe7f","EventType":"1","IsDayEvent":0,"StartTime":"09:00","SubTitle":"09:00 - 11:00","Subject":"公司周二例会"}]}]
     * Title : 08月15日
     */

    private String Title;
    private List<GroupsBean> Groups;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public List<GroupsBean> getGroups() {
        return Groups;
    }

    public void setGroups(List<GroupsBean> Groups) {
        this.Groups = Groups;
    }

    public static class GroupsBean {
        /**
         * GroupTitle : XXX的日程安排
         * list : [{"Done":0,"EndTime":"16:00","EventId":"0f6a07ad-3b2d-4bc9-b952-d1ac0ad63a3c","EventType":"1","IsDayEvent":0,"StartTime":"15:00","SubTitle":"15:00 - 16:00","Subject":"约见客户"},{"Done":0,"EndTime":"11:00","EventId":"b2429296-b290-4079-85ac-ef96d859fe7f","EventType":"1","IsDayEvent":0,"StartTime":"09:00","SubTitle":"09:00 - 11:00","Subject":"公司周二例会"}]
         */

        private String GroupTitle;
        private List<ListBean> list;

        public String getGroupTitle() {
            return GroupTitle;
        }

        public void setGroupTitle(String GroupTitle) {
            this.GroupTitle = GroupTitle;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * Done : 0
             * EndTime : 16:00
             * EventId : 0f6a07ad-3b2d-4bc9-b952-d1ac0ad63a3c
             * EventType : 1
             * IsDayEvent : 0
             * StartTime : 15:00
             * SubTitle : 15:00 - 16:00
             * Subject : 约见客户
             */

            private int Done;
            private String EndTime;
            private String EventId;
            private String EventType;
            private int IsDayEvent;
            private String StartTime;
            private String SubTitle;
            private String Subject;
            private String Checked;
            private String EventUrl;

            public String getEventUrl() {
                return EventUrl;
            }

            public void setEventUrl(String eventUrl) {
                EventUrl = eventUrl;
            }

            public String getChecked() {
                return Checked;
            }

            public void setChecked(String checked) {
                Checked = checked;
            }

            public int getDone() {
                return Done;
            }

            public void setDone(int Done) {
                this.Done = Done;
            }

            public String getEndTime() {
                return EndTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public String getEventId() {
                return EventId;
            }

            public void setEventId(String EventId) {
                this.EventId = EventId;
            }

            public String getEventType() {
                return EventType;
            }

            public void setEventType(String EventType) {
                this.EventType = EventType;
            }

            public int getIsDayEvent() {
                return IsDayEvent;
            }

            public void setIsDayEvent(int IsDayEvent) {
                this.IsDayEvent = IsDayEvent;
            }

            public String getStartTime() {
                return StartTime;
            }

            public void setStartTime(String StartTime) {
                this.StartTime = StartTime;
            }

            public String getSubTitle() {
                return SubTitle;
            }

            public void setSubTitle(String SubTitle) {
                this.SubTitle = SubTitle;
            }

            public String getSubject() {
                return Subject;
            }

            public void setSubject(String Subject) {
                this.Subject = Subject;
            }
        }
    }
}
