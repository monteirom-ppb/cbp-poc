package com.flutter.cbp.minhash.domain.part;

public class MarketTypeInfo {
    private Entity entity;

    public MarketTypeInfo(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "MarketTypeInfo{" +
                "entity=" + entity +
                '}';
    }
}
