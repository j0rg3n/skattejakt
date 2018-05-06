package com.Android2.controller;

import com.Android2.model.MapNode;

/**
 * Created by cirkus on 26.07.2017.
 */
abstract class MapNodeControllerBase<T extends MapNode> implements IMapNodeController {
    protected T node;

    MapNodeControllerBase(T node) {
        this.node = node;
    }

    @Override
    public MapNode getNode() {
        return node;
    }
}
