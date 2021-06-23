package com.hkzr.wlwd.model;

import com.hkzr.wlwd.ui.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macmini on 2017/7/13.
 */

public class VerifierEntity {
    private String _AutoID;
    private String GroupID;
    private String GroupName;
    private String MsgType;
    private String MsgTxt;
    private String UserId;
    private String UserCn;
    private String PhotoUrl;
    private String Result;
    private String _UserName;
    private String _CreateTime;

    public String get_AutoID() {
        return _AutoID;
    }

    public void set_AutoID(String _AutoID) {
        this._AutoID = _AutoID;
    }

    public String get_UserName() {
        return _UserName;
    }

    public void set_UserName(String _UserName) {
        this._UserName = _UserName;
    }

    public String get_CreateTime() {
        return _CreateTime;
    }

    public void set_CreateTime(String _CreateTime) {
        this._CreateTime = _CreateTime;
    }


    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getMsgTxt() {
        return MsgTxt;
    }

    public void setMsgTxt(String msgTxt) {
        MsgTxt = msgTxt;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserCn() {
        return UserCn;
    }

    public void setUserCn(String userCn) {
        UserCn = userCn;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }


    public static String get(JSONObject obj, String key) {
        String value = null;
        try {
            if (obj.has(key)) {
                value = obj.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.d("key:" + key);
        }

        return value;
    }

    public static VerifierEntity parse(JSONObject object) {

        VerifierEntity entity = new VerifierEntity();
        entity.set_AutoID(get(object, "_AutoID"));
        entity.setGroupID(get(object, "GroupID"));
        entity.setGroupName(get(object, "GroupName"));
        entity.setMsgType(get(object, "MsgType"));
        entity.setMsgTxt(get(object, "MsgTxt"));
        entity.setUserId(get(object, "UserId"));
        entity.setUserCn(get(object, "UserCn"));
        entity.setPhotoUrl(get(object, "PhotoUrl"));
        entity.setResult(get(object, "Result"));
        entity.set_UserName(get(object, "_UserName"));
        entity.set_CreateTime(get(object, "_CreateTime"));

        return entity;
    }

    public static List<VerifierEntity> parseArray(String json) {
        List<VerifierEntity> entitys = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                VerifierEntity entity = parse(array.getJSONObject(i));
                entitys.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return entitys;
    }

    @Override
    public String toString() {
        return "VerifierEntity{" +
                "_AutoID='" + _AutoID + '\'' +
                ", GroupID='" + GroupID + '\'' +
                ", GroupName='" + GroupName + '\'' +
                ", MsgType='" + MsgType + '\'' +
                ", MsgTxt='" + MsgTxt + '\'' +
                ", UserId='" + UserId + '\'' +
                ", UserCn='" + UserCn + '\'' +
                ", PhotoUrl='" + PhotoUrl + '\'' +
                ", Result='" + Result + '\'' +
                ", _UserName='" + _UserName + '\'' +
                ", _CreateTime='" + _CreateTime + '\'' +
                '}';
    }

}
