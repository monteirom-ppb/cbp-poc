package com.flutter.cbp.minhash.domain.part;

public class SportInfo {
    private Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public SportInfo(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "SportInfo{" +
                "entity=" + entity +
                '}';
    }
}
