package com.Android2.controller;

import com.Android2.model.MapArea;
import com.Android2.model.MapNode;
import com.Android2.model.POIMapNode;
import com.Android2.model.MyLocationMapNode;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by cirkus on 24.07.2017.
 */

public class PrimaryMapController implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCircleClickListener, GoogleMap.OnMarkerDragListener {
    public IMapNodeController selectedNode;

    public void removeNodeController(IMapNodeController mapNodeController) {
        if (mapNodeController == selectedNode) {
            clearSelectedNode();
        }
        model.remove(mapNodeController.getNode());
    }

    public boolean canRemoveSelectedNode() {
        return selectedNode instanceof IRemovable;
    }

    public void removeSelectedNode() {
        if (canRemoveSelectedNode()) {
            ((IRemovable)selectedNode).remove();
        }
    }

    public interface OnSelectionChangeListener {
        void onSelectionChanged();
    }

    public void setOnSelectionChanged(OnSelectionChangeListener onSelectionChanged) {
        this.onSelectionChanged = onSelectionChanged;
    }

    public OnSelectionChangeListener getOnSelectionChanged() {
        return onSelectionChanged;
    }

    public PrimaryMapController(MapArea model) {
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

        for (MapNode node : model.nodes) {
            addNodeController(node);
        }
    }

    public void refresh() {
        if (mMap != null) {
            for (MapNode node : model.nodes) {
                renderNode(node);
            }
        }
    }

    private void renderNode(MapNode node) {
        if (node.mapNodeController != null) {
            node.mapNodeController.render(mMap);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        POIMapNode newNode = model.addLockedNode(latLng);
        onNodeAdded(newNode);
    }

    private void onNodeAdded(MapNode newNode) {
        addNodeController(newNode);
        renderNode(newNode);
        setSelectedNode(newNode);
    }

    private void addNodeController(MapNode newNode) {
        if (newNode instanceof POIMapNode) {
            newNode.mapNodeController = new LockedNodeMapNodeController((POIMapNode)newNode, this);
        } else if (newNode instanceof MyLocationMapNode) {
            newNode.mapNodeController = new MyLocationMapNodeController((MyLocationMapNode) newNode);
        }
    }

    private void setSelectedNode(MapNode newNode) {
        if (newNode.mapNodeController != null) {
            setSelectedNode(newNode.mapNodeController);
        }
    }

    private void setSelectedNode(IMapNodeController newSelectedNode) {
        selectedNode = newSelectedNode;
        dispatchSelectionChanged();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Object tag = marker.getTag();
        if (tag instanceof IMapNodeController) {
            setSelectedNode((IMapNodeController) tag);
        } else {
            clearSelectedNode();
        }
        return dispatchClick(tag);
    }

    private void clearSelectedNode() {
        setSelectedNode((IMapNodeController) null);
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

    private void dispatchSelectionChanged() {
        if (onSelectionChanged != null) {
            onSelectionChanged.onSelectionChanged();
        }
    }

    private GoogleMap mMap;
    private MapArea model;
    private OnSelectionChangeListener onSelectionChanged;
}
