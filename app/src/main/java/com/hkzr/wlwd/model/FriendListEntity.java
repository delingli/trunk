package com.hkzr.wlwd.model;


public class FriendListEntity {
    private int FState;
    private int Flag;
    private String FriendCn;
    private String FriendId;
    private String KeyId;
    private String PhotoUrl;
    private String HelloTxt;

    public String getHelloTxt() {
        return HelloTxt;
    }

    public void setHelloTxt(String helloTxt) {
        HelloTxt = helloTxt;
    }

    public int getFState() {
        return FState;
    }

    public void setFState(int FState) {
        this.FState = FState;
    }

    public int getFlag() {
        return Flag;
    }

    public void setFlag(int Flag) {
        this.Flag = Flag;
    }

    public String getFriendCn() {
        return FriendCn;
    }

    public void setFriendCn(String FriendCn) {
        this.FriendCn = FriendCn;
    }

    public String getFriendId() {
        return FriendId;
    }

    public void setFriendId(String FriendId) {
        this.FriendId = FriendId;
    }

    public String getKeyId() {
        return KeyId;
    }

    public void setKeyId(String KeyId) {
        this.KeyId = KeyId;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
    }
}
