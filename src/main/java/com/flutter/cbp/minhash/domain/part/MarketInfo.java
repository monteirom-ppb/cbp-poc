package com.flutter.cbp.minhash.domain.part;

public class MarketInfo {
    private Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public MarketInfo(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "MarketInfo{" +
                "entity=" + entity +
                '}';
    }
}
