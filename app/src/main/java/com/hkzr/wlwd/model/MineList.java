package com.hkzr.wlwd.model;

/**
 * Created by admin on 2017/6/13.
 */

public class MineList {

    /**
     * FunCode :
     * FunIcon : http://dev.5lsoft.com/TJSJ/mobile/
     * FunLink : http://dev.5lsoft.com/TJSJ/mobile/SysHelp.aspx?tokenId={tokenid}
     * FunName : 系统帮助
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
