package com.Android2;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by cirkus on 25.07.2017.
 */

public class Model {
    public ArrayList<Node> nodes = new ArrayList<>();
    public Node myLocation = new MyLocationNode();

    public Model() {
        nodes.add(myLocation);

        LockedNode bauen = new LockedNode();
        bauen.location = new LatLng(59.12446, 11.18585);
        nodes.add(bauen);
    }

    @NonNull
    LockedNode addLockedNode(LatLng latLng) {
        LockedNode newNode = new LockedNode();
        newNode.location = latLng;
        nodes.add(newNode);
        return newNode;
    }
}
