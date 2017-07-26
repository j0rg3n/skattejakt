package com.Android2;

/**
 * Created by cirkus on 26.07.2017.
 */
abstract class MapNodeControllerBase<T extends Node> implements IMapNodeController {
    protected T node;

    MapNodeControllerBase(T node) {
        this.node = node;
    }

    @Override
    public Node getNode() {
        return node;
    }
}
