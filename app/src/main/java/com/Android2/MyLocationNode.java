package com.Android2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cirkus on 24.07.2017.
 */

public class MyLocationNode extends Node implements Parcelable {
    public MyLocationNode() {
        super();
    }

    private MyLocationNode(Parcel in)
    {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyLocationNode> CREATOR = new Creator<MyLocationNode>() {
        @Override
        public MyLocationNode createFromParcel(Parcel in) { return new MyLocationNode(in); }
        @Override
        public MyLocationNode[] newArray(int size) { return new MyLocationNode[size]; }
    };
}
