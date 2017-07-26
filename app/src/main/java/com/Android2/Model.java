package com.Android2;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by cirkus on 25.07.2017.
 */

public class Model {
    public ArrayList<Node> nodes = new ArrayList<>();
    public Node myLocation = new MyLocationNode(this);

    public Model() {
        nodes.add(myLocation);

        addLockedNode(new LatLng(59.12446, 11.18585));
    }

    @NonNull
    LockedNode addLockedNode(LatLng latLng) {
        LockedNode newNode = new LockedNode(this);
        newNode.location = latLng;
        nodes.add(newNode);
        return newNode;
    }

    public boolean remove(Node node) {
        return nodes.remove(node);
    }
}
