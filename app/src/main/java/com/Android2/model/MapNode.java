package com.Android2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.Android2.controller.IMapNodeController;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by cirkus on 24.07.2017.
 */

public class MapNode implements Parcelable {
    public LatLng location = null;
    public IMapNodeController mapNodeController = null;

    public MapNode() {
    }

    protected MapNode(Parcel in) {
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

    public static final Creator<MapNode> CREATOR = new Creator<MapNode>() {
        @Override
        public MapNode createFromParcel(Parcel in) { return new MapNode(in); }
        @Override
        public MapNode[] newArray(int size) { return new MapNode[size]; }
    };
}
