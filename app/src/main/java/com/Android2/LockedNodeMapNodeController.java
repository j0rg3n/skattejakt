package com.Android2;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by cirkus on 24.07.2017.
 */

public class LockedNodeMapNodeController implements IMapNodeController, IDraggable {
    public LockedNodeMapNodeController(LockedNode node) {
        this.node = node;
    }

    @Override
    public void render(GoogleMap map) {
        if (node.location != null) {
            if (marker != null) {
                marker.setPosition(node.location);
            } else {
                marker = map.addMarker(new MarkerOptions()
                        .position(node.location)
                        .title("Markør ved Bauen")
                        .draggable(true));
                marker.setTag(this);
            }
        } else {
            if (marker != null) {
                marker.remove();
            }
        }
    }

    @Override
    public boolean endDrag(LatLng newLocation) {
        node.location = newLocation;
        return true;
    }

    private LockedNode node;
    private Marker marker;
}
