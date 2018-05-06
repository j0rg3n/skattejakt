package com.Android2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cirkus on 24.07.2017.
 */

public class MyLocationMapNode extends MapNode implements Parcelable {
    public MyLocationMapNode() {
        super();
    }

    private MyLocationMapNode(Parcel in)
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

    public static final Creator<MyLocationMapNode> CREATOR = new Creator<MyLocationMapNode>() {
        @Override
        public MyLocationMapNode createFromParcel(Parcel in) { return new MyLocationMapNode(in); }
        @Override
        public MyLocationMapNode[] newArray(int size) { return new MyLocationMapNode[size]; }
    };
}
