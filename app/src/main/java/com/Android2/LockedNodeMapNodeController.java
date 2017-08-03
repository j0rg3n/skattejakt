package com.Android2;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by cirkus on 24.07.2017.
 */

public class LockedNodeMapNodeController extends MapNodeControllerBase<LockedNode> implements  IDraggable, IRemovable {
    public LockedNodeMapNodeController(LockedNode node, MapController mapController) {
        super(node);
        this.mapController = mapController;
    }

    @Override
    public void render(GoogleMap map) {
        if (node.location != null) {
            if (marker != null) {
                marker.setPosition(node.location);
                marker.setSnippet(getQRCodeDescription());
            } else {
                marker = map.addMarker(new MarkerOptions()
                        .position(node.location)
                        .title("Markør ved Bauen")
                        .snippet(getQRCodeDescription())
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bug_report_black_24dp)));
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
        if (marker != null) {
            marker.remove();
            marker = null;
        }
        mapController.removeNodeController(this);
    }

    @NonNull
    private String getQRCodeDescription() {
        return node.qrCode != null ? node.qrCode : "<ingen kode>";
    }

    private MapController mapController;
    private Marker marker;
}
