package com.hkzr.wlwd.ui.productlist;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;


public class Product implements Parcelable {
    public String id;
    public String producttype;
    public String Assembly;
    public String FramePart;

    public Product() {
    }

    protected Product(Parcel in) {
        id = in.readString();
        producttype = in.readString();
        Assembly = in.readString();
        FramePart = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(producttype);
        parcel.writeString(Assembly);
        parcel.writeString(FramePart);
    }
}
