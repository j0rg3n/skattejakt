package com.Android2;

/**
 * Created by cirkus on 24.07.2017.
 */

public class LockedNode extends Node {
    public LockedNode() {
        mapNodeController = new LockedNodeMapNodeController(this);
    }
}