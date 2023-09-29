package com.flutter.cbp.minhash.domain.part;

import java.util.Map;

public class Entity {
    private String name;
    private Map<String, String> ids;

    public String getName() {
        return name;
    }

    public Map<String, String> getIds() {
        return ids;
    }

    public Entity(String name, Map<String, String> ids) {
        this.name = name;
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", ids=" + ids +
                '}';
    }
}
