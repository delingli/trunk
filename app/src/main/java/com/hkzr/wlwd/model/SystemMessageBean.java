package com.hkzr.wlwd.model;

/**
 * Created by admin on 2017/6/20.
 */

public class SystemMessageBean {

    /**
     * msgId : 消息ID
     * msgType : 通知
     * icon : 头像URL
     * sender : 发送人
     * theme : blue
     * title : 你的会议审批管理员已经审批通过
     * content : HTML代码
     * funType : url
     * funPara : http://wwww.baidu.com
     */

    private String msgId;
    private String msgType;
    private String icon;
    private String sender;
    private String theme;
    private String title;
    private String content;
    private String funType;
    private String funPara;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFunType() {
        return funType;
    }

    public void setFunType(String funType) {
        this.funType = funType;
    }

    public String getFunPara() {
        return funPara;
    }

    public void setFunPara(String funPara) {
        this.funPara = funPara;
    }
}
