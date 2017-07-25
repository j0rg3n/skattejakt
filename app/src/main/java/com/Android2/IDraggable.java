package com.Android2;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by cirkus on 25.07.2017.
 */

public interface IDraggable {
    boolean endDrag(LatLng newLocation);
}
