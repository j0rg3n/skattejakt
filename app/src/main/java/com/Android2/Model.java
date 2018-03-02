package com.Android2;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by cirkus on 25.07.2017.
 */

public class Model implements Parcelable {
    public ArrayList<Node> nodes = new ArrayList<>();
    public Node myLocation = new MyLocationNode();

    public Model() {
        nodes.add(myLocation);
        addLockedNode(new LatLng(59.12446, 11.18585));
    }

    private Model(Parcel in) {
        //Parcelable[] nodesArray = in.readParcelableArray(null);
        //nodes.addAll(Arrays.copyOf(nodesArray, nodesArray.length, Node[].class));
        Collections.addAll(nodes, (Node[])in.readParcelableArray(null));
        //nodes = in.createTypedArrayList(Node.CREATOR); //< Cant do this, we need to sort by node type.
        myLocation = in.readParcelable(Node.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(nodes.toArray(new Parcelable[0]), 0);
        //dest.writeTypedList(nodes);
        dest.writeParcelable(myLocation, flags);
    }

    @NonNull
    LockedNode addLockedNode(LatLng latLng) {
        LockedNode newNode = new LockedNode();
        newNode.location = latLng;
        nodes.add(newNode);
        return newNode;
    }

    public boolean remove(Node node) {
        return nodes.remove(node);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };
}
