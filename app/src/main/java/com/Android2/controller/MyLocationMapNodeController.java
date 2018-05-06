package com.Android2.controller;

import android.graphics.Color;

import com.Android2.model.MyLocationMapNode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;

/**
 * Created by cirkus on 24.07.2017.
 */

public class MyLocationMapNodeController extends MapNodeControllerBase<MyLocationMapNode> {
    public MyLocationMapNodeController(MyLocationMapNode node) {
        super(node);
    }

    @Override
    public void render(GoogleMap map) {
        if (node.location != null) {
            if (circle == null) {
                circle = map.addCircle(new CircleOptions()
                        .center(node.location)
                        .radius(50.));
            } else {
                circle.setCenter(node.location);
            }
            circle.setStrokeColor(Color.BLUE);
        } else {
            if (circle != null) {
                circle.setStrokeColor(Color.RED);
                /*circle.remove();
                circle = null;*/
            }
        }

    }

    private Circle circle;
}
