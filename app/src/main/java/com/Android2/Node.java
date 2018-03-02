package com.Android2;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by cirkus on 24.07.2017.
 */

public class Node implements Parcelable {
    public LatLng location = null;
    public IMapNodeController mapNodeController = null;

    public Node() {
    }

    protected Node(Parcel in) {
        location = in.readParcelable(null);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(location, 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel in) { return new Node(in); }
        @Override
        public Node[] newArray(int size) { return new Node[size]; }
    };
}
