package com.flutter.cbp.minhash.domain.part;

public class SelectionInfo {
    private Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public SelectionInfo(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "SelectionInfo{" +
                "entity=" + entity +
                '}';
    }
}
