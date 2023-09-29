package com.flutter.cbp.minhash.domain.part;

public class EventInfo {
    private Entity entity;
    private String startTime;

    public Entity getEntity() {
        return entity;
    }

    public String getStartTime() {
        return startTime;
    }

    public EventInfo(Entity entity, String startTime) {
        this.entity = entity;
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "EventInfo{" +
                "entity=" + entity +
                ", startTime='" + startTime + '\'' +
                '}';
    }
}
