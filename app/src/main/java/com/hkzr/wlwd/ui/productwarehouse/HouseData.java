package com.hkzr.wlwd.ui.productwarehouse;

import android.os.Parcel;
import android.os.Parcelable;

public class HouseData implements Parcelable {
    //    "ID":"03a282a6-f15b-401b-96ca-de5be591f2ce",
//            "Name":"成品库房",
//            "Code":"0-05"
    public String ID;
    public String Name;
    public String Code;

    public HouseData() {
    }

    protected HouseData(Parcel in) {
        ID = in.readString();
        Name = in.readString();
        Code = in.readString();
    }

    public static final Creator<HouseData> CREATOR = new Creator<HouseData>() {
        @Override
        public HouseData createFromParcel(Parcel in) {
            return new HouseData(in);
        }

        @Override
        public HouseData[] newArray(int size) {
            return new HouseData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(Name);
        parcel.writeString(Code);
    }
}
