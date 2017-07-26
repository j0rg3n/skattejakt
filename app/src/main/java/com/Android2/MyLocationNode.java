package com.Android2;

/**
 * Created by cirkus on 24.07.2017.
 */

public class MyLocationNode extends Node {
    public MyLocationNode() {
        mapNodeController = new MyLocationMapNodeController(this);
    }
}
