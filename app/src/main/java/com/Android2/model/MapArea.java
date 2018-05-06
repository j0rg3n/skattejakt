package com.Android2.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by cirkus on 25.07.2017.
 */

public class MapArea implements Parcelable {
    public ArrayList<MapNode> nodes = new ArrayList<>();
    public MapNode myLocation = new MyLocationMapNode();

    public MapArea() {
        nodes.add(myLocation);
        addLockedNode(new LatLng(59.12446, 11.18585));
    }

    private MapArea(Parcel in) {
        //Parcelable[] nodesArray = in.readParcelableArray(null);
        //nodes.addAll(Arrays.copyOf(nodesArray, nodesArray.length, MapNode[].class));
        Collections.addAll(nodes, (MapNode[])in.readParcelableArray(null));
        //nodes = in.createTypedArrayList(MapNode.CREATOR); //< Cant do this, we need to sort by node type.
        myLocation = in.readParcelable(MapNode.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(nodes.toArray(new Parcelable[0]), 0);
        //dest.writeTypedList(nodes);
        dest.writeParcelable(myLocation, flags);
    }

    @NonNull
    public POIMapNode addLockedNode(LatLng latLng) {
        POIMapNode newNode = new POIMapNode();
        newNode.location = latLng;
        nodes.add(newNode);
        return newNode;
    }

    public boolean remove(MapNode node) {
        return nodes.remove(node);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MapArea> CREATOR = new Creator<MapArea>() {
        @Override
        public MapArea createFromParcel(Parcel in) {
            return new MapArea(in);
        }

        @Override
        public MapArea[] newArray(int size) {
            return new MapArea[size];
        }
    };
}
