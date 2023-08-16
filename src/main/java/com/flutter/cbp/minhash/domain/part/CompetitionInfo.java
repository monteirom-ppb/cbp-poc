package com.flutter.cbp.minhash.domain.part;

public class CompetitionInfo {
    private Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public CompetitionInfo(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "CompetitionInfo{" +
                "entity=" + entity +
                '}';
    }
}
