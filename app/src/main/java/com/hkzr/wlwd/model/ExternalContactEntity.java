package com.hkzr.wlwd.model;

/**
 * Created by admin on 2017/6/26.
 */

public class ExternalContactEntity {


    /**
     * LinkID : 2d922c4b-4c20-44ba-8365-f390f9156fb1
     * LinkName : 高见
     * CorpName : 石家庄软无忧信息科技有限公司
     * PosName :
     * Mobile : 15369181389
     * Manager : 管理员 负责
     * Remark : 标签1 标签2
     * Photo : http://dev.5lsoft.com/TJSJ/mobile/mobile.ashx?t=linkimg&photoId=
     */

    private String LinkID;
    private String LinkName;
    private String CorpName;
    private String PosName;
    private String Mobile;
    private String Manager;
    private String Remark;
    private String Photo;
    private String sortLetters;  //显示数据拼音的首字母

    public String getLinkID() {
        return LinkID;
    }

    public void setLinkID(String LinkID) {
        this.LinkID = LinkID;
    }

    public String getLinkName() {
        return LinkName;
    }

    public void setLinkName(String LinkName) {
        this.LinkName = LinkName;
    }

    public String getCorpName() {
        return CorpName;
    }

    public void setCorpName(String CorpName) {
        this.CorpName = CorpName;
    }

    public String getPosName() {
        return PosName;
    }

    public void setPosName(String PosName) {
        this.PosName = PosName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getManager() {
        return Manager;
    }

    public void setManager(String Manager) {
        this.Manager = Manager;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
