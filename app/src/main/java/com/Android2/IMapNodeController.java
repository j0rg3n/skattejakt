package com.Android2;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by cirkus on 24.07.2017.
 */

public interface IMapNodeController {
    void render(GoogleMap map);

    Node getNode();
}
