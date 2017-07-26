package com.Android2;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by cirkus on 24.07.2017.
 */

public class LockedNodeMapNodeController implements IMapNodeController, IDraggable, IRemovable {
    public LockedNodeMapNodeController(LockedNode node, MapController mapController) {
        this.node = node;
        this.mapController = mapController;
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

    @Override
    public void remove() {
        mapController.remove(this);
    }

    private LockedNode node;
    private MapController mapController;
    private Marker marker;
}
