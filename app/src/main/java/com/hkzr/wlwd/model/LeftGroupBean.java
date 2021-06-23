package com.hkzr.wlwd.model;

import java.util.List;

/**
 * 左侧菜单分级
 */

public class LeftGroupBean {


    /**
     * groups : [{"GroupCode":"MF01","GroupIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","GroupTitle":"公共应用","Layout":"2","MoreUrl":"","list":[{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/pen_basic_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/flowTodo.aspx?tokenId={tokenid}","FunName":"待办事项","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/bell_basic_yellow.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/appFrame/appList.aspx?tblName=T_OA_Note&tokenId={tokenid}","FunName":"通知公告","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/home_basic_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/appFrame/appList.aspx?tblName=T_PM_ProjectInfo&tokenId={tokenid}","FunName":"项目信息","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/web-cam_basic_red.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/appFrame/appList.aspx?tblName=T_PM_Qksb&tokenId={tokenid}","FunName":"项目监督","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/archive_basic_green.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"共享中心","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/web_basic_yellow.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Map/test.htm?tokenId={tokenid}","FunName":"地图定位","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/web_basic_yellow.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Map/MapLoad.aspx?tokenId={tokenid}","FunName":"项目地图","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/web_basic_yellow.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"统计分析","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Workasp/meeting/mymeeting.aspx?tokenId={tokenid}","FunName":"我的会议","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Workasp/meeting/1.html?tokenId={tokenid}","FunName":"收发文","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/custom/sp2.png","FunLink":"outsign","FunName":"签到","FunType":"app","Stamp":"","State":"是"}]},{"GroupCode":"MF03","GroupIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","GroupTitle":"项目管理","Layout":"2","MoreUrl":"","list":[{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/camera_basic_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/appFrame/appList.aspx?tblName=T_PM_RetributionApply&tokenId={tokenid}","FunName":"综合管理","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/document-edit_basic_yellow.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/appFrame/appList.aspx?tblName=T_PM_QualityInspection&tokenId={tokenid}","FunName":"投标管理","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/camera_basic_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/Extend/appList_ZLZG.aspx?tblName=T_PM_QualityRectification&tokenId={tokenid}","FunName":"进度管理","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/chart-area-up_basic_green.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/appFrame/appList.aspx?tblName=T_PM_Ydkh&tokenId={tokenid}","FunName":"质量管理","FunType":"url","Stamp":"","State":"是"},{"FunCode":"ZLGL001","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/appFrame/AppSecPage.aspx?mainid=ZLGL001&tokenId={tokenid}","FunName":"安全管理","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"技术管理","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"成本管理","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Workasp/contact/newcontact.aspx?tokenId={tokenid}","FunName":"测试","FunType":"url","Stamp":"","State":"是"}]},{"GroupCode":"","GroupIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","GroupTitle":"船舶管理","Layout":"2","MoreUrl":"","list":[{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/shield_basic_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/appFrame/appList.aspx?tblName=T_PM_SafeInspection&tokenId={tokenid}","FunName":"船舶信息","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/shield_basic_red.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/extend/appList_AQZG.aspx??tblName=T_PM_SafeRectification&tokenId={tokenid}","FunName":"船舶动态","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"船舶生产","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"船舶物资","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"船舶监控","FunType":"url","Stamp":"","State":"是"}]},{"GroupCode":"","GroupIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","GroupTitle":"合同管理","Layout":"2","MoreUrl":"","list":[{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"相关方申请","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"合同查询","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"新签合同分析","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"专也分包合同","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"劳务分包合同","FunType":"url","Stamp":"","State":"是"}]}]
     * topbar : {"Show":true,"list":[{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"工单","FunType":"url","Stamp":"","State":"是"},{"FunCode":"flowTodo","FunIcon":"http://cdn.5lsoft.com/AppRes/img/custom/sp1.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"审批","FunType":"url","Stamp":"4","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/custom/sp2.png","FunLink":"checkin","FunName":"考勤","FunType":"app","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/custom/scan2.png","FunLink":"scancode","FunName":"扫一扫","FunType":"app","Stamp":"","State":"是"}]}
     */

    private TopbarBean topbar;
    private List<GroupsBean> groups;

    public TopbarBean getTopbar() {
        return topbar;
    }

    public void setTopbar(TopbarBean topbar) {
        this.topbar = topbar;
    }

    public List<GroupsBean> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupsBean> groups) {
        this.groups = groups;
    }

    public static class TopbarBean {
        /**
         * Show : true
         * list : [{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"工单","FunType":"url","Stamp":"","State":"是"},{"FunCode":"flowTodo","FunIcon":"http://cdn.5lsoft.com/AppRes/img/custom/sp1.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"审批","FunType":"url","Stamp":"4","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/custom/sp2.png","FunLink":"checkin","FunName":"考勤","FunType":"app","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/custom/scan2.png","FunLink":"scancode","FunName":"扫一扫","FunType":"app","Stamp":"","State":"是"}]
         */

        private boolean Show;
        private List<ListBean> list;

        public boolean isShow() {
            return Show;
        }

        public void setShow(boolean Show) {
            this.Show = Show;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * FunCode :
             * FunIcon : http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png
             * FunLink : http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}
             * FunName : 工单
             * FunType : url
             * Stamp :
             * State : 是
             */

            private String FunCode;
            private String FunIcon;
            private String FunLink;
            private String FunName;
            private String FunType;
            private String Stamp;
            private String State;

            public String getFunCode() {
                return FunCode;
            }

            public void setFunCode(String FunCode) {
                this.FunCode = FunCode;
            }

            public String getFunIcon() {
                return FunIcon;
            }

            public void setFunIcon(String FunIcon) {
                this.FunIcon = FunIcon;
            }

            public String getFunLink() {
                return FunLink;
            }

            public void setFunLink(String FunLink) {
                this.FunLink = FunLink;
            }

            public String getFunName() {
                return FunName;
            }

            public void setFunName(String FunName) {
                this.FunName = FunName;
            }

            public String getFunType() {
                return FunType;
            }

            public void setFunType(String FunType) {
                this.FunType = FunType;
            }

            public String getStamp() {
                return Stamp;
            }

            public void setStamp(String Stamp) {
                this.Stamp = Stamp;
            }

            public String getState() {
                return State;
            }

            public void setState(String State) {
                this.State = State;
            }
        }
    }

    public static class GroupsBean {
        /**
         * GroupCode : MF01
         * GroupIcon : http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png
         * GroupTitle : 公共应用
         * Layout : 2
         * MoreUrl :
         * list : [{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/pen_basic_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/flowTodo.aspx?tokenId={tokenid}","FunName":"待办事项","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/bell_basic_yellow.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/appFrame/appList.aspx?tblName=T_OA_Note&tokenId={tokenid}","FunName":"通知公告","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/home_basic_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/appFrame/appList.aspx?tblName=T_PM_ProjectInfo&tokenId={tokenid}","FunName":"项目信息","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/web-cam_basic_red.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Sysfolder/appFrame/appList.aspx?tblName=T_PM_Qksb&tokenId={tokenid}","FunName":"项目监督","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/archive_basic_green.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"共享中心","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/web_basic_yellow.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Map/test.htm?tokenId={tokenid}","FunName":"地图定位","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/web_basic_yellow.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Map/MapLoad.aspx?tokenId={tokenid}","FunName":"项目地图","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/super_mono/web_basic_yellow.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/?tokenId={tokenid}","FunName":"统计分析","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Workasp/meeting/mymeeting.aspx?tokenId={tokenid}","FunName":"我的会议","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://dev.5lsoft.com/TJSJ/mobile/img/empty/coffee_blue.png","FunLink":"http://dev.5lsoft.com/TJSJ/mobile/Workasp/meeting/1.html?tokenId={tokenid}","FunName":"收发文","FunType":"url","Stamp":"","State":"是"},{"FunCode":"","FunIcon":"http://cdn.5lsoft.com/AppRes/img/custom/sp2.png","FunLink":"outsign","FunName":"签到","FunType":"app","Stamp":"","State":"是"}]
         */

        private String GroupCode;
        private String GroupIcon;
        private String GroupTitle;
        private String Layout;
        private String MoreUrl;
        private List<ListBeanX> list;

        public String getGroupCode() {
            return GroupCode;
        }

        public void setGroupCode(String GroupCode) {
            this.GroupCode = GroupCode;
        }

        public String getGroupIcon() {
            return GroupIcon;
        }

        public void setGroupIcon(String GroupIcon) {
            this.GroupIcon = GroupIcon;
        }

        public String getGroupTitle() {
            return GroupTitle;
        }

        public void setGroupTitle(String GroupTitle) {
            this.GroupTitle = GroupTitle;
        }

        public String getLayout() {
            return Layout;
        }

        public void setLayout(String Layout) {
            this.Layout = Layout;
        }

        public String getMoreUrl() {
            return MoreUrl;
        }

        public void setMoreUrl(String MoreUrl) {
            this.MoreUrl = MoreUrl;
        }

        public List<ListBeanX> getList() {
            return list;
        }

        public void setList(List<ListBeanX> list) {
            this.list = list;
        }

        public static class ListBeanX {
            /**
             * FunCode :
             * FunIcon : http://cdn.5lsoft.com/AppRes/img/super_mono/pen_basic_blue.png
             * FunLink : http://dev.5lsoft.com/TJSJ/mobile/flowTodo.aspx?tokenId={tokenid}
             * FunName : 待办事项
             * FunType : url
             * Stamp :
             * State : 是
             */

            private String FunCode;
            private String FunIcon;
            private String FunLink;
            private String FunName;
            private String FunType;
            private String Stamp;
            private String State;

            public String getFunCode() {
                return FunCode;
            }

            public void setFunCode(String FunCode) {
                this.FunCode = FunCode;
            }

            public String getFunIcon() {
                return FunIcon;
            }

            public void setFunIcon(String FunIcon) {
                this.FunIcon = FunIcon;
            }

            public String getFunLink() {
                return FunLink;
            }

            public void setFunLink(String FunLink) {
                this.FunLink = FunLink;
            }

            public String getFunName() {
                return FunName;
            }

            public void setFunName(String FunName) {
                this.FunName = FunName;
            }

            public String getFunType() {
                return FunType;
            }

            public void setFunType(String FunType) {
                this.FunType = FunType;
            }

            public String getStamp() {
                return Stamp;
            }

            public void setStamp(String Stamp) {
                this.Stamp = Stamp;
            }

            public String getState() {
                return State;
            }

            public void setState(String State) {
                this.State = State;
            }
        }
    }
}
