package com.Android2;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cirkus on 24.07.2017.
 */

public class MapController implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCircleClickListener, GoogleMap.OnMarkerDragListener {
    private List<IMapNodeController> mapRenderers = new ArrayList<>();
    private GoogleMap mMap;
    private Model model;

    public MapController(Model model) {
        this.model = model;
    }

    public void setup(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnCircleClickListener(this);

        LatLng bauen = new LatLng(59.12446, 11.18585);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bauen, 16));
    }

    public void refresh() {
        if (mMap != null) {
            for (Node node : model.nodes) {
                renderNode(node);
            }
        }
    }

    private void renderNode(Node node) {
        if (node.mapNodeController != null) {
            node.mapNodeController.render(mMap);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        LockedNode newNode = model.addLockedNode(latLng);
        renderNode(newNode);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return dispatchClick(marker.getTag());
    }

    @Override
    public void onCircleClick(Circle circle) {
        dispatchClick(circle.getId()); // TODO: Map to actual controller instance.
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        // ignore
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        // ignore
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        dispatchDrag(marker.getTag(), marker.getPosition());
    }

    private boolean dispatchClick(Object tag) {
        if (tag instanceof IClickable) {
            return ((IClickable)tag).click();
        }
        return false;
    }

    private boolean dispatchDrag(Object tag, LatLng newLocation) {
        if (tag instanceof IDraggable) {
            return ((IDraggable)tag).endDrag(newLocation);
        }
        return false;
    }

}
